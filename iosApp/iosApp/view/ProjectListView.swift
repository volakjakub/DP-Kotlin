import SwiftUI
import Shared

struct ProjectListView: View {
    let biographyService: BiographyService
    let biography: BiographyResponse
    let account: AccountResponse
    @Binding var skills: [SkillResponse]
    var updateSkills: ([SkillResponse]) -> Void

    @State private var isLoadingProjects = true
    @State private var error: String?
    @State private var showProjectForm = false
    @State private var projects: [ProjectResponse] = []
    @State private var projectEdit: ProjectResponse? = nil

    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Projekty")
                .font(.system(size: 20, weight: .bold))
                .foregroundColor(.black)
                .padding(.top, 15)
                .padding(.bottom, 10)
            Button(action: {
                projectEdit = nil
                showProjectForm = true
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

            if isLoadingProjects {
                ProgressView()
                    .progressViewStyle(CircularProgressViewStyle(tint: .blue))
                    .frame(maxWidth: .infinity, alignment: .center)
            }

            if !projects.isEmpty {
                ForEach(Array(projects.enumerated()), id: \.offset) { index, project in
                    AnimatedExpandableProjectBox(project: project, projectEdit: $projectEdit, onDeleteSubmit: onDeleteSubmit)

                    if index < projects.count - 1 {
                        Divider()
                            .background(Color.gray)
                            .padding(.vertical, 5)
                    }
                }
            }
        }
        .onAppear(perform: loadProjects)
        .onChange(of: projectEdit) { newValue in
            // Automatically open the sheet when an item is set
            showProjectForm = newValue != nil
        }
        .sheet(isPresented: $showProjectForm, onDismiss: {
            projectEdit = nil
        }) {
            ProjectFormDialog(
                isVisible: $showProjectForm,
                onSubmit: onFormSubmit,
                onDismiss: {
                    projectEdit = nil
                    showProjectForm = false
                },
                existingProject: projectEdit,
                skills: skills
            )
        }
    }

    private func loadProjects() {
        isLoadingProjects = true
        Task {
            do {
                let loadedProjects = try await biographyService.getProjectsByBiography(biographyId: Int(biography.id))
                projects = loadedProjects
            } catch {
                self.error = error.localizedDescription
            }
            isLoadingProjects = false
        }
    }

    private func onShowForm(project: ProjectResponse) {
        DispatchQueue.main.async {
            projectEdit = project
            showProjectForm = true
        }
    }

    private func onDeleteSubmit(project: ProjectResponse) {
        isLoadingProjects = true
        Task {
            do {
                let success = try await biographyService.deleteProject(projectId: Int(project.id))
                if success {
                    projects.removeAll { $0.id == project.id }
                } else {
                    error = "Projekt se nepodařilo odstranit."
                }
            } catch {
                self.error = error.localizedDescription
            }
            isLoadingProjects = false
        }
    }

    private func onFormSubmit(request: ProjectRequest) {
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

        isLoadingProjects = true
        Task {
            do {
                if request.id == nil {
                    let created = try await biographyService.createProject(request: mutableRequest)
                    projects.append(created)
                } else {
                    let updated = try await biographyService.updateProject(request: mutableRequest)
                    if let index = projects.firstIndex(where: { $0.id == updated.id }) {
                        projects[index] = updated
                    }
                }
                
                updateSkills(try await biographyService.getSkillsByBiography(biographyId: Int(biography.id)))
            } catch {
                self.error = error.localizedDescription
            }
            isLoadingProjects = false
        }
    }
}
