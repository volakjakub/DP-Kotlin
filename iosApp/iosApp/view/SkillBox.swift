import SwiftUI
import Shared

struct SkillBox: View {
    var skill: SkillResponse
    @Binding var skillEdit: SkillResponse?
    let onDeleteSubmit: (SkillResponse) -> Void
    // Initialize the helper
    let expertiseHelper = ExpertiseHelper()
    
    @State private var isExpanded = false
    @State private var showDeleteMessage = false
    
    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            HStack {
                VStack(alignment: .leading, spacing: 4) {
                    Text(skill.name)
                        .font(.system(size: 14, weight: .bold))
                        .foregroundColor(.black)
                    
                    Text(expertiseHelper.getExpertise(expertise: skill.expertise))
                        .font(.system(size: 14, weight: .regular))
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
                        Text("Opravdu chcete odstranit tuto dovednost?")
                            .font(.system(size: 14, weight: .bold))
                            .foregroundColor(.black)
                            .padding(.trailing, 10)
                        Spacer()
                        Button(action: {
                            onDeleteSubmit(skill)
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
                    HStack {
                        Spacer()
                        Button(action: {
                            skillEdit = skill
                        }) {
                            Text("Upravit")
                                .foregroundColor(.white)
                        }
                        .buttonStyle(.borderedProminent)
                        .tint(.blue)

                        if (skill.projects.isEmpty) {
                            Spacer().frame(width: 8)
                            
                            Button(action: {
                                showDeleteMessage = true
                            }) {
                                Text("Odstranit")
                            }
                            .buttonStyle(.borderedProminent)
                            .tint(.red)
                        }
                    }
                    .padding(7)
                }
            }
        }
        .padding(8)
        .background(Color.gray.opacity(0.2))
        .cornerRadius(8)
    }
}
