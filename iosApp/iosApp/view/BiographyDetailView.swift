import SwiftUI
import Shared

struct BiographyDetailView: View {
    let biographyService: BiographyService
    let account: AccountResponse
    
    @State private var isNewUser = false
    @State private var showForm = false
    @State private var isLoadingBiography = true
    @State private var error: String? = nil
    @State private var biography: BiographyResponse? = nil

    var body: some View {
        VStack {
            if let error = error {
                Text(error)
                    .foregroundColor(.red)
                    .font(.system(size: 12))
                    .multilineTextAlignment(.center)
                    .padding()
            }
            if isLoadingBiography {
                ProgressView()
                    .progressViewStyle(CircularProgressViewStyle(tint: .blue))
                    .padding()
            }
            if isNewUser && !showForm {
                VStack(spacing: 16) {
                    Text("Ještě nemáte v aplikaci vytvořený životopis. Vytvořte si ho prosím.")
                        .multilineTextAlignment(.center)
                        .font(.system(size: 15))
                        .padding()

                    Button(action: {
                        showForm = true
                    }) {
                        Text("Vytvořit")
                            .foregroundColor(.white)
                            .frame(maxWidth: .infinity)
                            .padding()
                            .background(Color.blue)
                            .cornerRadius(8)
                    }
                }
                .padding()
            }
            
            if let bio = biography {
                ScrollView {
                    VStack(spacing: 16) {
                        BiographyInfo(biography: bio)

                        Button(action: {
                            showForm = true
                        }) {
                            Text("Upravit")
                                .foregroundColor(.white)
                                .frame(maxWidth: .infinity)
                                .padding()
                                .background(Color.blue)
                                .cornerRadius(8)
                        }

                        LanguageListView(biographyService: biographyService, biography: bio, account: account)
                        EducationListView(biographyService: biographyService, biography: bio, account: account)
                        ProjectListView(service: biographyService, biography: bio)
                        SkillListView(service: biographyService, biography: bio)
                    }
                    .padding()
                    .sheet(isPresented: $showForm) {
                        BiographyFormView(
                            biography: biography,
                            account: account,
                            onSubmit: { request in
                                Task {
                                    await handleFormSubmit(request: request)
                                }
                            },
                            onClose: {
                                showForm = false
                            }
                        )
                    }
                }
            }
        }
        .onAppear {
            Task {
                await loadBiography()
            }
        }
    }

    private func loadBiography() async {
        self.isLoadingBiography = true
        do {
            self.biography = try await biographyService.getBiography()
            self.isNewUser = false
        } catch let error as BiographyServiceError {
            // Handle specific BiographyServiceError cases
            switch error {
            case .notFoundError:
                self.biography = nil
                self.isNewUser = true
            case .loadingError, .authError:
                self.error = error.localizedDescription
            }
        } catch {
            // Handle any unexpected errors
            self.error = error.localizedDescription
        }
        self.isLoadingBiography = false
    }

    private func handleFormSubmit(request: BiographyRequest) async {
        self.isLoadingBiography = true
        do {
            if request.id == nil {
                self.biography = try await biographyService.createBiography(request)
            } else {
                self.biography = try await biographyService.updateBiography(request)
            }
            self.isNewUser = false
            self.showForm = false
        } catch {
            self.error = error.localizedDescription
        }
        self.isLoadingBiography = false
    }
}
