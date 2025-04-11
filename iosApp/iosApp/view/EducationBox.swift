import SwiftUI
import Shared

struct EducationBox: View {
    let education: EducationResponse
    @Binding var educationEdit: EducationResponse?
    let onDeleteSubmit: (EducationResponse) -> Void
    // Initialize the helper
    let educationHelper = EducationHelper()
    
    @State private var isExpanded = false
    @State private var showDeleteMessage = false
    
    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            HStack {
                VStack(alignment: .leading, spacing: 4) {
                    Text(education.school)
                        .font(.system(size: 14, weight: .bold))
                        .foregroundColor(.black)
                    
                    Text(educationHelper.getType(type: education.type))
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
            
            if (isExpanded) {
                VStack(alignment: .leading, spacing: 4) {
                    let startFormatted = formatDate(parseDate(from: education.start))
                    Text("Od: \(startFormatted)")
                        .font(.system(size: 14, weight: .regular))
                        .foregroundColor(.black)
                    
                    let endFormatted = formatDate(parseDate(from: education.end))
                    Text("Do: \(endFormatted)")
                        .font(.system(size: 14, weight: .regular))
                        .foregroundColor(.black)
                }
                .padding(7)
                
                Divider()
                    .background(Color.black)
                    .padding(.vertical, 5)
                
                if showDeleteMessage {
                    HStack {
                        Text("Opravdu chcete odstranit toto vzdělání?")
                            .font(.system(size: 14, weight: .bold))
                            .foregroundColor(.black)
                            .padding(.trailing, 10)
                        Spacer()
                        Button(action: {
                            onDeleteSubmit(education)
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
                            educationEdit = education
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
        .padding(8)
        .background(Color.gray.opacity(0.2))
        .cornerRadius(8)
    }
}
