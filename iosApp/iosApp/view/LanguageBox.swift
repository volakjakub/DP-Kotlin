import SwiftUICore
import Shared

struct LanguageBox: View {
    var language: LanguageResponse
    
    // Initialize helpers
    let languageHelper = LanguageHelper()
    let expertiseHelper = ExpertiseHelper()
    
    init(language: LanguageResponse) {
        self.language = language
    }
    
    var body: some View {
        ZStack {
            Color.gray
            HStack {
                VStack(alignment: .leading) {
                    Text(languageHelper.getLanguage(language: language.name))
                        .font(.system(size: 14, weight: .bold))
                        .foregroundColor(.black)
                    
                    Text(expertiseHelper.getExpertise(expertise: language.expertise))
                        .font(.system(size: 14, weight: .regular))
                        .foregroundColor(.black)
                }
                .padding(7)
                
                Spacer()
            }
            .frame(maxWidth: .infinity, alignment: .bottom)
        }
        .cornerRadius(8)
        .padding(.horizontal, 10)
    }
}
