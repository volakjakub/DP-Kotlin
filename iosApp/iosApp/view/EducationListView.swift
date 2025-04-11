import SwiftUI
import Shared

struct EducationListView: View {
    let biographyService: BiographyService
    let biography: BiographyResponse
    let account: AccountResponse

    @State private var isLoadingEducations = true
    @State private var error: String?
    @State private var showEducationForm = false
    @State private var educations: [EducationResponse] = []
    @State private var educationEdit: EducationResponse? = nil

    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Vzdělání")
                .font(.system(size: 20, weight: .bold))
                .foregroundColor(.black)
                .padding(.top, 15)
                .padding(.bottom, 10)

            Button(action: {
                educationEdit = nil
                showEducationForm = true
            }) {
                Text("Vytvořit")
                    .foregroundColor(.white)
                    .padding()
                    .frame(maxWidth: .infinity)
                    .background(Color.blue)
                    .cornerRadius(8)
            }
            
            if let error = error {
                Text(error)
                    .foregroundColor(.red)
                    .font(.footnote)
                    .frame(maxWidth: .infinity, alignment: .center)
            }

            if isLoadingEducations {
                ProgressView()
                    .progressViewStyle(CircularProgressViewStyle(tint: .blue))
                    .frame(maxWidth: .infinity, alignment: .center)
            }

            if !educations.isEmpty {
                ForEach(Array(educations.enumerated()), id: \.offset) { index, education in
                    EducationBox(education: education, educationEdit: $educationEdit, onDeleteSubmit: onDeleteSubmit)

                    if index < educations.count - 1 {
                        Divider()
                            .background(Color.gray)
                            .padding(.vertical, 5)
                    }
                }
            }
        }
        .onAppear(perform: loadEducations)
        .onChange(of: educationEdit) { newValue in
            // Automatically open the sheet when an item is set
            showEducationForm = newValue != nil
        }
        .sheet(isPresented: $showEducationForm, onDismiss: {
            educationEdit = nil
        }) {
            EducationFormDialog(
                isVisible: $showEducationForm,
                onSubmit: onFormSubmit,
                onDismiss: {
                    educationEdit = nil
                    showEducationForm = false
                },
                existingEducation: educationEdit
            )
        }
    }

    private func loadEducations() {
        isLoadingEducations = true
        Task {
            do {
                let loadedEducations = try await biographyService.getEducationsByBiography(biographyId: Int(biography.id))
                educations = loadedEducations
            } catch {
                self.error = error.localizedDescription
            }
            isLoadingEducations = false
        }
    }

    private func onShowForm(education: EducationResponse) {
        DispatchQueue.main.async {
            educationEdit = education
            showEducationForm = true
        }
    }

    private func onDeleteSubmit(education: EducationResponse) {
        isLoadingEducations = true
        Task {
            do {
                let success = try await biographyService.deleteEducation(educationId: Int(education.id))
                if success {
                    educations.removeAll { $0.id == education.id }
                } else {
                    error = "Vzdělání se nepodařilo odstranit."
                }
            } catch {
                self.error = error.localizedDescription
            }
            isLoadingEducations = false
        }
    }

    private func onFormSubmit(request: EducationRequest) {
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

        var mutableRequest = request
        mutableRequest.biography = biographyRequest

        isLoadingEducations = true
        Task {
            do {
                if request.id == nil {
                    let created = try await biographyService.createEducation(request: mutableRequest)
                    educations.append(created)
                } else {
                    let updated = try await biographyService.updateEducation(request: mutableRequest)
                    if let index = educations.firstIndex(where: { $0.id == updated.id }) {
                        educations[index] = updated
                    }
                }
            } catch {
                self.error = error.localizedDescription
            }
            isLoadingEducations = false
        }
    }
}
