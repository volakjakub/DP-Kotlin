import SwiftUI
import Shared

struct EducationFormDialog: View {
    @Binding var isVisible: Bool
    var onSubmit: (EducationRequest) -> Void
    var onDismiss: () -> Void
    var existingEducation: EducationResponse?
    
    let educationHelper = EducationHelper()

    @State private var selectedSchool: String = ""
    @State private var selectedType: String = ""
    @State private var selectedStart: Date? = nil
    @State private var selectedEnd: Date? = nil
    @State private var showStartDatePicker = false
    @State private var showEndDatePicker = false

    @State private var showTypePicker = false

    private let typeOptions = [Education.highSchool, Education.bachelor, Education.master, Education.doctorate]

    var body: some View {
        if isVisible {
            ZStack {
                Color.white.edgesIgnoringSafeArea(.all)
                VStack(spacing: 8) {
                    Text("Přidat vzdělání")
                        .font(.headline)

                    Group {
                        TextField("Škola", text: $selectedSchool)
                        Menu {
                            ForEach(typeOptions, id: \.displayName) { type in
                                Button(action: {
                                    selectedType = type.displayName
                                }) {
                                    Text(educationHelper.getType(type: type.displayName))
                                }
                            }
                        } label: {
                            HStack {
                                Text(educationHelper.getType(type: selectedType))
                                Spacer()
                                Image(systemName: "chevron.down")
                            }
                            .padding()
                            .background(Color(UIColor.systemGray6))
                            .cornerRadius(8)
                        }
                        VStack(alignment: .leading) {
                            Text("Počátek studia: \(formatDate(selectedStart))")
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
                            Text("Ukončení studia: \(formatDate(selectedEnd))")
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
                    }
                    .textFieldStyle(RoundedBorderTextFieldStyle())

                    HStack {
                        Spacer()
                        if (selectedSchool != "" && selectedType != "" && selectedStart != nil) {
                            Button(action: {
                                let educationRequestWrapper: EducationRequestWrapper = EducationRequestWrapper()
                                if (existingEducation == nil) {
                                    let request = educationRequestWrapper.createEducationRequest(
                                        school: selectedSchool,
                                        type: selectedType,
                                        start: selectedStart != nil ? selectedStart!.toBackendFormat() : "",
                                        end: selectedEnd?.toBackendFormat()
                                    )
                                    onSubmit(request)
                                } else {
                                    let request = educationRequestWrapper.updateEducationRequest(
                                        id: Int32(existingEducation!.id),
                                        school: selectedSchool,
                                        type: selectedType,
                                        start: selectedStart != nil ? selectedStart!.toBackendFormat() : "",
                                        end: selectedEnd?.toBackendFormat()
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
                .onAppear {
                    selectedSchool = existingEducation?.school ?? ""
                    selectedType = existingEducation?.type ?? ""
                    selectedStart = parseDate(from: existingEducation?.start)
                    selectedEnd = parseDate(from: existingEducation?.end)
                }
            }
        }
    }
}
