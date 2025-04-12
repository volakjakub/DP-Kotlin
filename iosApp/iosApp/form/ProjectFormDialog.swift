import SwiftUI
import Shared

struct ProjectFormDialog: View {
    @Binding var isVisible: Bool
    var onSubmit: (ProjectRequest) -> Void
    var onDismiss: () -> Void
    var existingProject: ProjectResponse?
    var skills: [SkillResponse]

    @State private var selectedName: String = ""
    @State private var selectedClient: String = ""
    @State private var selectedDesc: String = ""
    @State private var selectedStart: Date? = nil
    @State private var selectedEnd: Date? = nil
    @State private var selectedSkills: [Int: Bool] = [:]
    @State private var showStartDatePicker = false
    @State private var showEndDatePicker = false

    var body: some View {
        if isVisible {
            ZStack {
                ScrollView {
                    Color.white.edgesIgnoringSafeArea(.all)
                    VStack(spacing: 8) {
                        Text("Přidat projekt")
                            .font(.headline)
                        
                        Group {
                            TextField("Název:", text: $selectedName)
                            TextField("Klient:", text: $selectedClient)
                            TextField("Popis:", text: $selectedDesc)
                            
                            VStack(alignment: .leading) {
                                Text("Počátek projektu: \(formatDate(selectedStart))")
                                Button("Vybrat datum počátku") {
                                    showStartDatePicker = !showStartDatePicker
                                }
                                .frame(maxWidth: .infinity)
                                .padding()
                                .background(Color.blue)
                                .foregroundColor(.white)
                                .cornerRadius(8)
                            }
                            if showStartDatePicker {
                                DatePicker(
                                    "Vyberte datum počátku",
                                    selection: Binding(get: {
                                        selectedStart ?? Date()
                                    }, set: {
                                        selectedStart = $0
                                    }),
                                    displayedComponents: .date
                                )
                                .datePickerStyle(.graphical)
                                .padding()
                            }
                            VStack(alignment: .leading) {
                                Text("Ukončení projektu: \(formatDate(selectedEnd))")
                                Button("Vybrat datum ukončení") {
                                    showEndDatePicker = !showEndDatePicker
                                }
                                .frame(maxWidth: .infinity)
                                .padding()
                                .background(Color.blue)
                                .foregroundColor(.white)
                                .cornerRadius(8)
                            }
                            if showEndDatePicker {
                                DatePicker(
                                    "Vyberte datum ukončení",
                                    selection: Binding(get: {
                                        selectedEnd ?? Date()
                                    }, set: {
                                        selectedEnd = $0
                                    }),
                                    displayedComponents: .date
                                )
                                .datePickerStyle(.graphical)
                                .padding()
                            }
                            
                            Spacer().frame(height: 8)
                            
                            Text("Dovednosti:")
                                .frame(maxWidth: .infinity, alignment: .leading)
                                .padding(.vertical, 8)
                            
                            ForEach(skills, id: \.id) { skill in
                                HStack(alignment: .center) {
                                    Button(action: {
                                        selectedSkills[Int(skill.id)]?.toggle()
                                    }) {
                                        Image(systemName: selectedSkills[Int(skill.id)] == true ? "checkmark.square" : "square")
                                            .foregroundColor(.blue)
                                    }
                                    
                                    Text(skill.name)
                                        .padding(.leading, 8)
                                    
                                    Spacer()
                                }
                                .padding(.vertical, 4)
                            }
                        }
                        .textFieldStyle(RoundedBorderTextFieldStyle())
                        
                        HStack {
                            Spacer()
                            if (selectedName != "" && selectedClient != "" && selectedStart != nil) {
                                Button(action: {
                                    let projectRequestWrapper: ProjectRequestWrapper = ProjectRequestWrapper()
                                    if (existingProject == nil) {
                                        let request = projectRequestWrapper.createProjectRequest(
                                            name: selectedName,
                                            client: selectedClient,
                                            start: selectedStart != nil ? selectedStart!.toBackendFormat() : "",
                                            end: selectedEnd?.toBackendFormat(),
                                            desc: selectedDesc,
                                            skills: skills
                                                .filter { selectedSkills[Int($0.id)] == true }
                                                .map { skill in ProjectSkillRequest(id: skill.id) }
                                        )
                                        onSubmit(request)
                                    } else {
                                        let request = projectRequestWrapper.updateProjectRequest(
                                            id: Int32(existingProject!.id),
                                            name: selectedName,
                                            client: selectedClient,
                                            start: selectedStart != nil ? selectedStart!.toBackendFormat() : "",
                                            end: selectedEnd?.toBackendFormat(),
                                            desc: selectedDesc,
                                            skills: skills
                                                .filter { selectedSkills[Int($0.id)] == true }
                                                .map { skill in ProjectSkillRequest(id: skill.id) }
                                        )
                                        onSubmit(request)
                                    }
                                    isVisible = false
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
                        selectedName = existingProject?.name ?? ""
                        selectedClient = existingProject?.client ?? ""
                        selectedDesc = existingProject?.getDesc() ?? ""
                        selectedStart = parseDate(from: existingProject?.start)
                        selectedEnd = parseDate(from: existingProject?.end)
                        
                        var initialSelection: [Int: Bool] = [:]
                        for skill in skills {
                            let isSelected = existingProject?.skills.contains(where: { $0.id == skill.id }) ?? false
                            initialSelection[Int(skill.id)] = isSelected
                        }
                        selectedSkills = initialSelection
                    }
                }
            }
        }
    }
}
