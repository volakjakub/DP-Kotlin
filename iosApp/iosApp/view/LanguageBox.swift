import SwiftUICore
import SwiftUI
import Shared

struct LanguageBox: View {
    let language: LanguageResponse
    @Binding var languageEdit: LanguageResponse?
    let onDeleteSubmit: (LanguageResponse) -> Void
    let canEdit: Bool

    @State private var isExpanded = false
    @State private var showDeleteMessage = false

    var body: some View {
        let languageHelper = LanguageHelper()
        let expertiseHelper = ExpertiseHelper()

        VStack(alignment: .leading, spacing: 8) {
            HStack {
                VStack(alignment: .leading, spacing: 4) {
                    Text(languageHelper.getLanguage(language: language.name))
                        .font(.system(size: 14, weight: .bold))
                        .foregroundColor(.black)

                    Text(expertiseHelper.getExpertise(expertise: language.expertise))
                        .font(.system(size: 14))
                        .foregroundColor(.black)
                }
                Spacer()
            }
            .padding(7)
            .contentShape(Rectangle())
            .onTapGesture {
                withAnimation(.easeInOut(duration: 0.3)) {
                    isExpanded.toggle()
                    showDeleteMessage = false
                }
            }

            if isExpanded {
                Divider()
                    .background(Color.black)
                    .padding(.vertical, 5)

                if showDeleteMessage {
                    HStack {
                        Text("Opravdu chcete odstranit tento jazyk?")
                            .font(.system(size: 14, weight: .bold))
                            .foregroundColor(.black)
                            .padding(.trailing, 10)
                        Spacer()
                        Button(action: {
                            onDeleteSubmit(language)
                            showDeleteMessage = false
                        }) {
                            Text("Odstranit")
                                .foregroundColor(.white)
                        }
                        .buttonStyle(.borderedProminent)
                        .tint(.red)
                    }
                    .padding(7)
                } else {
                    if (canEdit) {
                        HStack {
                            Spacer()
                            Button(action: {
                                languageEdit = language
                            }) {
                                Text("Upravit")
                                    .foregroundColor(.white)
                            }
                            .buttonStyle(.borderedProminent)
                            .tint(.blue)

                            Spacer().frame(width: 8)

                            Button(action: {
                                showDeleteMessage = true
                            }) {
                                Text("Odstranit")
                            }
                            .buttonStyle(.borderedProminent)
                            .tint(.red)
                        }
                        .padding(7)
                    }
                }
            }
        }
        .padding(8)
        .background(Color.gray.opacity(0.2))
        .cornerRadius(8)
    }
}
