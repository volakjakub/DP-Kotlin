import SwiftUI
import Shared

struct AnimatedExpandableProjectBox: View {
    let project: ProjectResponse
    @Binding var projectEdit: ProjectResponse?
    let onDeleteSubmit: (ProjectResponse) -> Void
    
    @State private var isExpanded: Bool = false
    @State private var showDeleteMessage = false
    
    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            HStack {
                VStack(alignment: .leading) {
                    Text(project.name)
                        .font(.system(size: 14, weight: .bold))
                        .foregroundColor(.black)
                    
                    Text(project.client)
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
                VStack(alignment: .leading, spacing: 4) {
                    Text("Od: \(formatDate(parseDate(from: project.start)))")
                        .font(.system(size: 14, weight: .regular))
                        .foregroundColor(.black)
                    
                    Text("Do: \(formatDate(parseDate(from: project.end)))")
                        .font(.system(size: 14, weight: .regular))
                        .foregroundColor(.black)
                    
                    if let desc = project.getDesc() {
                        Text("Popis: " + desc)
                            .font(.system(size: 14, weight: .regular))
                            .foregroundColor(.black)
                    }
                    
                    Text("Dovednosti projektu: " + project.skills.map { $0.name }.joined(separator: ", "))
                        .font(.system(size: 13, weight: .regular))
                        .foregroundColor(.black)
                }
                .padding(7)
                
                Divider()
                    .background(Color.black)
                    .padding(.vertical, 5)
                
                if showDeleteMessage {
                    HStack {
                        Text("Opravdu chcete odstranit tento projekt?")
                            .font(.system(size: 14, weight: .bold))
                            .foregroundColor(.black)
                            .padding(.trailing, 10)
                        Spacer()
                        Button(action: {
                            onDeleteSubmit(project)
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
                            projectEdit = project
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
