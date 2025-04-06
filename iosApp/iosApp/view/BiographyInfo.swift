import SwiftUI
import Shared

struct BiographyInfo: View {
    let biography: BiographyResponse

    var body: some View {
        let employedFromDate = parseDate(from: biography.employedFrom)
        let employedFromFormatted = formatDate(employedFromDate)

        VStack(alignment: .leading, spacing: 8) {
            Group {
                LabelView(label: "Titul", value: biography.title ?? "")
                LabelView(label: "Jméno", value: biography.firstName)
                LabelView(label: "Příjmení", value: biography.lastName)
                LabelView(label: "E-Mail", value: biography.email)
                LabelView(label: "Telefon", value: biography.phone)
            }

            Divider().padding(.vertical, 8)

            Group {
                LabelView(label: "Ulice", value: biography.street)
                LabelView(label: "Město", value: biography.city)
                LabelView(label: "Stát", value: biography.country)
            }

            Divider().padding(.vertical, 8)

            Group {
                LabelView(label: "Pracovní pozice", value: biography.position)
                LabelView(label: "Zaměstnán/a od", value: employedFromFormatted)
            }
        }
    }

    private func parseDate(from string: String) -> Date? {
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd"
        return formatter.date(from: string)
    }

    private func formatDate(_ date: Date?) -> String {
        guard let date = date else { return "-" }
        let formatter = DateFormatter()
        formatter.dateFormat = "dd.MM.yyyy"
        return formatter.string(from: date)
    }
}

struct LabelView: View {
    let label: String
    let value: String

    var body: some View {
        VStack(alignment: .leading, spacing: 2) {
            Text(label)
                .font(.system(size: 14, weight: .bold))
                .foregroundColor(.black)
            Text(value)
                .font(.system(size: 14))
                .foregroundColor(.black)
        }
    }
}
