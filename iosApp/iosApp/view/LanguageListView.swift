import SwiftUI
import Shared

struct LanguageListView: View {
    let service: BiographyService
    let biography: BiographyResponse

    @State private var languages: [LanguageResponse] = []
    @State private var isLoading = true
    @State private var error: String?
    
    init(service: BiographyService, biography: BiographyResponse) {
        self.service = service
        self.biography = biography
    }

    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Jazyky")
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

            if !languages.isEmpty {
                ForEach(Array(languages.enumerated()), id: \.offset) { index, language in
                    LanguageBox(language: language)

                    if index < languages.count - 1 {
                        Divider()
                            .background(Color.gray)
                            .padding(.vertical, 5)
                    }
                }
            }
        }
        .onAppear {
            Task {
                await loadLanguages()
            }
        }
    }

    private func loadLanguages() async {
        do {
            languages = try await service.getLanguagesByBiography(biographyId: Int(biography.id))
        } catch {
            self.error = error.localizedDescription
        }
        isLoading = false
    }
}
