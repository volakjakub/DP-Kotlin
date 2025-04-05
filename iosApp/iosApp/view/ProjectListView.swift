import SwiftUI
import Shared

struct ProjectListView: View {
    let service: BiographyService
    let biography: BiographyResponse

    @State private var projects: [ProjectResponse] = []
    @State private var isLoading = true
    @State private var error: String?
    
    init(service: BiographyService, biography: BiographyResponse) {
        self.service = service
        self.biography = biography
    }

    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Projekty")
                .font(.system(size: 20, weight: .bold))
                .foregroundColor(.black)
                .padding(.top, 15)
                .padding(.bottom, 10)

            if let error = error {
                Text(error)
                    .foregroundColor(.red)
                    .font(.system(size: 12))
                    .frame(maxWidth: .infinity, alignment: .center)
            }

            if isLoading {
                ProgressView()
                    .progressViewStyle(CircularProgressViewStyle(tint: .blue))
                    .frame(maxWidth: .infinity, alignment: .center)
            }

            if !projects.isEmpty {
                ForEach(Array(projects.enumerated()), id: \.offset) { index, project in
                    AnimatedExpandableProjectBox(project: project)

                    if index < projects.count - 1 {
                        Divider()
                            .background(Color.gray)
                            .padding(.vertical, 5)
                    }
                }
            }
        }
        .onAppear {
            Task {
                await loadProjects()
            }
        }
    }

    private func loadProjects() async {
        do {
            projects = try await service.getProjectsByBiography(biographyId: Int(biography.id))
        } catch {
            self.error = error.localizedDescription
        }
        isLoading = false
    }
}
