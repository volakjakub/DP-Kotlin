import SwiftUI
import Shared

struct SkillListView: View {
    let service: BiographyService
    let biography: BiographyResponse

    @State private var skills: [SkillResponse] = []
    @State private var isLoading = true
    @State private var error: String?
    
    init(service: BiographyService, biography: BiographyResponse) {
        self.service = service
        self.biography = biography
    }

    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Dovednosti")
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

            if !skills.isEmpty {
                ForEach(Array(skills.enumerated()), id: \.offset) { index, skill in
                    SkillBox(skill: skill)

                    if index < skills.count - 1 {
                        Divider()
                            .background(Color.gray)
                            .padding(.vertical, 5)
                    }
                }
            }
        }
        .onAppear {
            Task {
                await loadSkills()
            }
        }
    }

    private func loadSkills() async {
        do {
            skills = try await service.getSkillsByBiography(biographyId: Int(biography.id))
        } catch {
            self.error = error.localizedDescription
        }
        isLoading = false
    }
}
