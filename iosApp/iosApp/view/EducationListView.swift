import SwiftUI
import Shared

struct EducationListView: View {
    let service: BiographyService
    let biography: BiographyResponse

    @State private var educations: [EducationResponse] = []
    @State private var isLoading = true
    @State private var error: String?
    
    init(service: BiographyService, biography: BiographyResponse) {
        self.service = service
        self.biography = biography
    }

    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Vzdělání")
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

            if !educations.isEmpty {
                ForEach(Array(educations.enumerated()), id: \.offset) { index, education in
                    EducationBox(education: education)

                    if index < educations.count - 1 {
                        Divider()
                            .background(Color.gray)
                            .padding(.vertical, 5)
                    }
                }
            }
        }
        .onAppear {
            Task {
                await loadEducations()
            }
        }
    }

    private func loadEducations() async {
        do {
            educations = try await service.getEducationsByBiography(biographyId: Int(biography.id))
        } catch {
            self.error = error.localizedDescription
        }
        isLoading = false
    }
}
