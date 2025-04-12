import SwiftUI
import Shared

struct LanguageListView: View {
    let biographyService: BiographyService
    let biography: BiographyResponse
    let account: AccountResponse
    let canEdit: Bool

    @State private var isLoadingLanguages = true
    @State private var error: String?
    @State private var showLanguageForm = false
    @State private var languages: [LanguageResponse] = []
    @State private var languageEdit: LanguageResponse?

    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Jazyky")
                .font(.system(size: 20, weight: .bold))
                .foregroundColor(.black)
                .padding(.top, 15)

            if (canEdit) {
                Button(action: {
                    languageEdit = nil
                    showLanguageForm = true
                }) {
                    Text("Vytvořit")
                        .foregroundColor(.white)
                        .padding()
                        .frame(maxWidth: .infinity)
                        .background(Color.blue)
                        .cornerRadius(8)
                }
            }

            if let error = error {
                Text(error)
                    .foregroundColor(.red)
                    .font(.footnote)
                    .frame(maxWidth: .infinity, alignment: .center)
            }

            if isLoadingLanguages {
                ProgressView()
                    .progressViewStyle(CircularProgressViewStyle(tint: .blue))
                    .frame(maxWidth: .infinity, alignment: .center)
            }

            if !languages.isEmpty {
                ForEach(Array(languages.enumerated()), id: \.offset) { index, language in
                    LanguageBox(language: language, languageEdit: $languageEdit, onDeleteSubmit: onDeleteSubmit, canEdit: canEdit)

                    if index < languages.count - 1 {
                        Divider()
                            .background(Color.gray)
                            .padding(.vertical, 5)
                    }
                }
            }
        }
        .onAppear(perform: loadLanguages)
        .onChange(of: languageEdit) { newValue in
            // Automatically open the sheet when an item is set
            showLanguageForm = newValue != nil
        }
        .sheet(isPresented: $showLanguageForm) {
            LanguageFormDialog(
                isVisible: $showLanguageForm,
                onSubmit: onFormSubmit,
                onDismiss: {
                    languageEdit = nil
                    showLanguageForm = false
                },
                existingLanguage: languageEdit,
                languageList: languages
            )
        }
    }

    private func loadLanguages() {
        isLoadingLanguages = true
        Task {
            do {
                let loadedLanguages = try await biographyService.getLanguagesByBiography(biographyId: Int(biography.id))
                languages = loadedLanguages
            } catch {
                self.error = error.localizedDescription
            }
            isLoadingLanguages = false
        }
    }

    private func onDeleteSubmit(language: LanguageResponse) {
        isLoadingLanguages = true
        Task {
            do {
                let success = try await biographyService.deleteLanguage(languageId: Int(language.id))
                if success {
                    languages.removeAll { $0.id == language.id }
                } else {
                    error = "Jazyk se nepodařilo odstranit."
                }
            } catch {
                self.error = error.localizedDescription
            }
            isLoadingLanguages = false
        }
    }

    private func onFormSubmit(request: LanguageRequest) {
        let biographyRequestWrapper: BiographyRequestWrapper = BiographyRequestWrapper()
        let user = BiographyUserRequest(id: account.id, login: account.login)
        let biographyRequest = biographyRequestWrapper.updateBiographyRequest(
            id: biography.id,
            title: biography.title ?? "",
            firstName: biography.firstName,
            lastName: biography.lastName,
            phone: biography.phone,
            email: biography.email,
            street: biography.street,
            city: biography.city,
            country: biography.country,
            position: biography.position,
            employedFrom: biography.employedFrom,
            user: user
        )

        let mutableRequest = request
        mutableRequest.biography = biographyRequest

        isLoadingLanguages = true
        Task {
            do {
                if request.id == nil {
                    let created = try await biographyService.createLanguage(request: mutableRequest)
                    languages.append(created)
                } else {
                    let updated = try await biographyService.updateLanguage(request: mutableRequest)
                    if let index = languages.firstIndex(where: { $0.id == updated.id }) {
                        languages[index] = updated
                    }
                }
            } catch {
                self.error = error.localizedDescription
            }
            isLoadingLanguages = false
        }
    }
}
