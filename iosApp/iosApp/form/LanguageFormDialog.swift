import SwiftUI
import Shared

struct LanguageFormDialog: View {
    @Binding var isVisible: Bool
    var onSubmit: (LanguageRequest) -> Void
    var onDismiss: () -> Void
    var existingLanguage: LanguageResponse?
    var languageList: [LanguageResponse]
    
    let languageHelper = LanguageHelper()
    let expertiseHelper = ExpertiseHelper()

    @State private var selectedLanguage: String = ""
    @State private var selectedExpertise: Int = 1

    @State private var showLanguagePicker = false
    @State private var showExpertisePicker = false

    private let languageOptions = [Language.czech, Language.english, Language.slovak]
    private let expertiseOptions = [1, 2, 3, 4, 5]

    var body: some View {
        if isVisible {
            ZStack {
                Color.white.edgesIgnoringSafeArea(.all)
                VStack(spacing: 8) {
                    Text("Přidat jazyk")
                        .font(.headline)

                    // Language Dropdown
                    Menu {
                        ForEach(languageOptions.filter { name in
                            !languageList.contains(where: { $0.name == name.displayName }) || name.displayName == existingLanguage?.name
                        }, id: \.self) { name in
                            Button(action: {
                                selectedLanguage = name.displayName
                            }) {
                                Text(languageHelper.getLanguage(language: name.displayName))
                            }
                        }
                    } label: {
                        HStack {
                            Text(languageHelper.getLanguage(language: selectedLanguage))
                            Spacer()
                            Image(systemName: "chevron.down")
                        }
                        .padding()
                        .background(Color(UIColor.systemGray6))
                        .cornerRadius(8)
                    }

                    // Expertise Dropdown
                    Menu {
                        ForEach(expertiseOptions, id: \.self) { level in
                            Button(action: {
                                selectedExpertise = level
                            }) {
                                Text(expertiseHelper.getExpertise(expertise: Int32(level)))
                            }
                        }
                    } label: {
                        HStack {
                            Text(expertiseHelper.getExpertise(expertise: Int32(selectedExpertise)))
                            Spacer()
                            Image(systemName: "chevron.down")
                        }
                        .padding()
                        .background(Color(UIColor.systemGray6))
                        .cornerRadius(8)
                    }

                    HStack {
                        Spacer()
                        if (selectedLanguage != "") {
                            Button(action: {
                                let languageRequestWrapper: LanguageRequestWrapper = LanguageRequestWrapper()
                                if !selectedLanguage.isEmpty {
                                    if (existingLanguage == nil) {
                                        let request = languageRequestWrapper.createLanguageRequest(
                                            name: selectedLanguage,
                                            expertise: Int32(selectedExpertise)
                                        )
                                        onSubmit(request)
                                    } else {
                                        let request = languageRequestWrapper.updateLanguageRequest(
                                            id: Int32(existingLanguage!.id),
                                            name: selectedLanguage,
                                            expertise: Int32(selectedExpertise)
                                        )
                                        onSubmit(request)
                                    }
                                    isVisible = false
                                }
                            }) {
                                Text("Uložit")
                                    .padding()
                                    .background(Color.blue)
                                    .foregroundColor(.white)
                                    .cornerRadius(8)
                            }
                        }

                        Button(action: {
                            onDismiss()
                        }) {
                            Text("Zpět")
                                .padding()
                                .background(Color.gray.opacity(0.2))
                                .cornerRadius(8)
                        }
                    }
                }
                .padding()
                .background(Color.white)
                .cornerRadius(12)
                .padding()
                .onAppear {
                    selectedLanguage = existingLanguage?.name ?? ""
                    selectedExpertise = Int(existingLanguage?.expertise ?? 1)
                }
            }
        }
    }
}
