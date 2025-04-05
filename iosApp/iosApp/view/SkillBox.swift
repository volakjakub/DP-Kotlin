import SwiftUICore
import Shared

struct SkillBox: View {
    var skill: SkillResponse
    // Initialize the helper
    let expertiseHelper = ExpertiseHelper()
    
    init(skill: SkillResponse) {
        self.skill = skill
    }
    
    var body: some View {
        ZStack {
            Color.gray
            HStack {
                VStack(alignment: .leading) {
                    Text(skill.name)
                        .font(.system(size: 14, weight: .bold))
                        .foregroundColor(.black)
                    
                    Text(expertiseHelper.getExpertise(expertise: skill.expertise))
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
