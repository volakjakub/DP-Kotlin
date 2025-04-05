import SwiftUI
import Shared

struct EducationBox: View {
    var education: EducationResponse
    // Initialize the helper
    let educationHelper = EducationHelper()
    
    init(education: EducationResponse) {
        self.education = education
    }
    
    // Helper function to format the date string
    func formatDate(_ dateStr: String) -> String {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd" // Assuming input date is in this format
        if let date = dateFormatter.date(from: dateStr) {
            let formattedDate = DateFormatter()
            formattedDate.dateFormat = "dd.MM.yyyy"
            return formattedDate.string(from: date)
        }
        return ""
    }
    
    var body: some View {
        ZStack {
            Color.gray
            HStack {
                VStack(alignment: .leading) {
                    Text(education.school)
                        .font(.system(size: 14, weight: .bold))
                        .foregroundColor(.black)
                    
                    Text(educationHelper.getType(type: education.type))
                        .font(.system(size: 14, weight: .regular))
                        .foregroundColor(.black)
                    
                    let startFormatted = formatDate(education.start)
                    Text("Od: \(startFormatted)")
                        .font(.system(size: 14, weight: .regular))
                        .foregroundColor(.black)
                    
                    // Check if end date is available and display accordingly
                    if let end = education.end {
                        let endFormatted = formatDate(end)
                        Text("Do: \(endFormatted)")
                            .font(.system(size: 14, weight: .regular))
                            .foregroundColor(.black)
                    } else {
                        Text("Do: -")
                            .font(.system(size: 14, weight: .regular))
                            .foregroundColor(.black)
                    }
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
