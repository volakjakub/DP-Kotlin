import SwiftUI
import Shared

struct SkillListView: View {
    let biographyService: BiographyService
    let biography: BiographyResponse
    let account: AccountResponse
    @Binding var skills: [SkillResponse]
    var updateSkills: ([SkillResponse]) -> Void
    let canEdit: Bool

    @State private var isLoadingSkills = false
    @State private var error: String?
    @State private var showSkillForm = false
    @State private var skillEdit: SkillResponse?

    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Dovednosti")
                .font(.system(size: 20, weight: .bold))
                .foregroundColor(.black)
                .padding(.top, 15)
                .padding(.bottom, 10)

            if (canEdit) {
                Button(action: {
                    skillEdit = nil
                    showSkillForm = true
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

            if isLoadingSkills {
                ProgressView()
                    .progressViewStyle(CircularProgressViewStyle(tint: .blue))
                    .frame(maxWidth: .infinity, alignment: .center)
            }

            if !skills.isEmpty {
                ForEach(Array(skills.enumerated()), id: \.offset) { index, skill in
                    SkillBox(skill: skill, skillEdit: $skillEdit, onDeleteSubmit: onDeleteSubmit, canEdit: canEdit)

                    if index < skills.count - 1 {
                        Divider()
                            .background(Color.gray)
                            .padding(.vertical, 5)
                    }
                }
            }
        }
        .onChange(of: skillEdit) { newValue in
            // Automatically open the sheet when an item is set
            showSkillForm = newValue != nil
        }
        .sheet(isPresented: $showSkillForm) {
            SkillFormDialog(
                isVisible: $showSkillForm,
                onSubmit: onFormSubmit,
                onDismiss: {
                    skillEdit = nil
                    showSkillForm = false
                },
                existingSkill: skillEdit,
                skillList: skills
            )
        }
    }

    private func onDeleteSubmit(skill: SkillResponse) {
        isLoadingSkills = true
        Task {
            do {
                let success = try await biographyService.deleteSkill(skillId: Int(skill.id))
                if success {
                    skills.removeAll { $0.id == skill.id }
                    updateSkills(skills)
                } else {
                    error = "Jazyk se nepodařilo odstranit."
                }
            } catch {
                self.error = error.localizedDescription
            }
            isLoadingSkills = false
        }
    }

    private func onFormSubmit(request: SkillRequest) {
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

        isLoadingSkills = true
        Task {
            do {
                if request.id == nil {
                    let created = try await biographyService.createSkill(request: mutableRequest)
                    skills.append(created)
                    updateSkills(skills)
                } else {
                    let updated = try await biographyService.updateSkill(request: mutableRequest)
                    if let index = skills.firstIndex(where: { $0.id == updated.id }) {
                        skills[index] = updated
                        updateSkills(skills)
                    }
                }
            } catch {
                self.error = error.localizedDescription
            }
            isLoadingSkills = false
        }
    }
}
