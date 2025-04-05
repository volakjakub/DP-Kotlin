import SwiftUI
import Shared

struct BiographyDetailView: View {
    @State private var biography: BiographyResponse?
    @State private var error: String?
    @State private var isLoading = true
    
    let biographyService: BiographyService
    
    init(biographyService: BiographyService) {
        self.biographyService = biographyService
    }
    
    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 12) {
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

                if let bio = biography {
                    Group {
                        field(title: "Titul", value: bio.title)
                        field(title: "Jméno", value: bio.firstName)
                        field(title: "Příjmení", value: bio.lastName)
                        field(title: "E-Mail", value: bio.email)
                        field(title: "Telefon", value: bio.phone)

                        Divider().padding(.vertical, 8)

                        field(title: "Ulice", value: bio.street)
                        field(title: "Město", value: bio.city)
                        field(title: "Stát", value: bio.country)

                        Divider().padding(.vertical, 8)

                        field(title: "Pracovní pozice", value: bio.position)
                        
                        if let date = formatDate(bio.employedFrom) {
                            field(title: "Zaměstnán/a od", value: date)
                        }

                        LanguageListView(service: biographyService, biography: bio)
                        EducationListView(service: biographyService, biography: bio)
                        ProjectListView(service: biographyService, biography: bio)
                        SkillListView(service: biographyService, biography: bio)
                    }
                }
            }
            .padding()
        }
        .background(Color.white)
        .onAppear {
            Task {
                await loadBiography()
            }
        }
    }

    private func loadBiography() async {
        do {
            biography = try await biographyService.getBiography()
        } catch {
            self.error = error.localizedDescription
        }
        isLoading = false
    }

    @ViewBuilder
    private func field(title: String, value: String?) -> some View {
        Text(title)
            .font(.system(size: 14, weight: .bold))
            .foregroundColor(.black)
        Text(value ?? "")
            .font(.system(size: 14))
            .foregroundColor(.black)
    }

    private func formatDate(_ dateString: String) -> String? {
        let inputFormatter = DateFormatter()
        inputFormatter.dateFormat = "yyyy-MM-dd"

        let outputFormatter = DateFormatter()
        outputFormatter.dateFormat = "dd.MM.yyyy"

        if let date = inputFormatter.date(from: dateString) {
            return outputFormatter.string(from: date)
        }
        return nil
    }
}
