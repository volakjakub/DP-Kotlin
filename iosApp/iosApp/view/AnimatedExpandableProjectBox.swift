import SwiftUICore
import Shared

struct AnimatedExpandableProjectBox: View {
    var project: ProjectResponse
    @State private var isExpanded: Bool = false
    
    init(project: ProjectResponse) {
        self.project = project
    }

    var startFormatted: String {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd"
        if let date = dateFormatter.date(from: project.start) {
            dateFormatter.dateFormat = "dd.MM.yyyy"
            return dateFormatter.string(from: date)
        }
        return ""
    }
    
    // Format the end date if exists
    var endFormatted: String? {
        if let endDate = project.end {
            let dateFormatter = DateFormatter()
            dateFormatter.dateFormat = "yyyy-MM-dd"
            if let date = dateFormatter.date(from: endDate) {
                dateFormatter.dateFormat = "dd.MM.yyyy"
                return dateFormatter.string(from: date)
            }
        }
        return nil
    }
    
    var body: some View {
        VStack {
            HStack {
                VStack(alignment: .leading) {
                    Text(project.name)
                        .font(.system(size: 14, weight: .bold))
                        .foregroundColor(.black)
                    
                    Text(project.client)
                        .font(.system(size: 14, weight: .regular))
                        .foregroundColor(.black)
                    
                    if isExpanded {
                        Text("Od: \(startFormatted)")
                            .font(.system(size: 14, weight: .regular))
                            .foregroundColor(.black)
                        
                        if let endFormatted = endFormatted {
                            Text("Do: \(endFormatted)")
                                .font(.system(size: 14, weight: .regular))
                                .foregroundColor(.black)
                        } else {
                            Text("Do: -")
                                .font(.system(size: 14, weight: .regular))
                                .foregroundColor(.black)
                        }

                        if let desc = project.getDesc() {
                            Text("Popis: " + desc)
                                .font(.system(size: 14, weight: .regular))
                                .foregroundColor(.black)
                        }
                        
                        Text("Dovednosti projektu: " + project.skills.map { $0.name }.joined(separator: ", "))
                            .font(.system(size: 13, weight: .regular))
                            .foregroundColor(.black)
                    }
                }
                Spacer()
            }
            .padding(7)
            .background(Color.gray)
            .cornerRadius(8)
            .onTapGesture {
                withAnimation(.easeInOut(duration: 0.3)) {
                    isExpanded.toggle()
                }
            }
            .padding(.horizontal, 10)
        }
        .animation(.easeInOut(duration: 0.3), value: isExpanded) // Animation on expanding/collapsing
    }
}
