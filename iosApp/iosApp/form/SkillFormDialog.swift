import SwiftUI
import Shared

struct SkillFormDialog: View {
    @Binding var isVisible: Bool
    var onSubmit: (SkillRequest) -> Void
    var onDismiss: () -> Void
    var existingSkill: SkillResponse?
    var skillList: [SkillResponse]
    
    let expertiseHelper = ExpertiseHelper()

    @State private var selectedSkill: String = ""
    @State private var selectedExpertise: Int = 1

    @State private var showExpertisePicker = false

    private let expertiseOptions = [1, 2, 3, 4, 5]

    var body: some View {
        if isVisible {
            ZStack {
                Color.white.edgesIgnoringSafeArea(.all)
                VStack(spacing: 8) {
                    Text("Přidat dovednost")
                        .font(.headline)

                    TextField("Název", text: $selectedSkill)
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
                        if (selectedSkill != "") {
                            Button(action: {
                                let skillRequestWrapper: SkillRequestWrapper = SkillRequestWrapper()
                                if !selectedSkill.isEmpty {
                                    if (existingSkill == nil) {
                                        let request = skillRequestWrapper.createSkillRequest(
                                            name: selectedSkill,
                                            expertise: Int32(selectedExpertise)
                                        )
                                        onSubmit(request)
                                    } else {
                                        let request = skillRequestWrapper.updateSkillRequest(
                                            id: Int32(existingSkill!.id),
                                            name: selectedSkill,
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
                    selectedSkill = existingSkill?.name ?? ""
                    selectedExpertise = Int(existingSkill?.expertise ?? 1)
                }
            }
        }
    }
}
