import Foundation
import SwiftUI
import Shared

extension Date {
    func toBackendFormat() -> String {
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd"
        return formatter.string(from: self)
    }
}

struct BiographyFormView: View {
    let biography: BiographyResponse?
    let account: AccountResponse
    var onSubmit: (BiographyRequest) -> Void
    var onClose: () -> Void

    @State private var title: String = ""
    @State private var firstName: String = ""
    @State private var lastName: String = ""
    @State private var phone: String = ""
    @State private var email: String = ""
    @State private var street: String = ""
    @State private var city: String = ""
    @State private var country: String = ""
    @State private var position: String = ""
    @State private var employedFrom: Date? = nil
    @State private var showDatePicker = false

    var body: some View {
        ScrollView {
            VStack(spacing: 12) {
                inputFields

                employedFromSection

                if showDatePicker {
                    DatePicker(
                        "Vyberte datum",
                        selection: Binding(get: {
                            employedFrom ?? Date()
                        }, set: {
                            employedFrom = $0
                        }),
                        displayedComponents: .date
                    )
                    .datePickerStyle(.graphical)
                    .padding()
                }

                saveButton

                if biography != nil {
                    backButton
                }
            }
            .padding()
            .onAppear(perform: initializeForm)
        }
    }

    private var inputFields: some View {
        Group {
            TextField("Titul", text: $title)
            TextField("Jméno", text: $firstName)
            TextField("Příjmení", text: $lastName)
            TextField("Telefon", text: $phone)
            TextField("E-mail", text: $email)
            TextField("Ulice", text: $street)
            TextField("Město", text: $city)
            TextField("Stát", text: $country)
            TextField("Pozice", text: $position)
        }
        .textFieldStyle(RoundedBorderTextFieldStyle())
    }

    private var employedFromSection: some View {
        VStack(alignment: .leading) {
            Text("Zaměstnán/a od: \(formatDate(employedFrom))")
            Button("Vybrat datum") {
                showDatePicker = true
            }
            .frame(maxWidth: .infinity)
            .padding()
            .background(Color.blue)
            .foregroundColor(.white)
            .cornerRadius(8)
        }
    }

    private var saveButton: some View {
        Button("Uložit") {
            handleSubmit()
        }
    }
    
    private func handleSubmit() {
        let biographyRequestWrapper: BiographyRequestWrapper = BiographyRequestWrapper()
        let u = BiographyUserRequest(id: account.id, login: account.login)
        if (biography == nil) {
            let request = biographyRequestWrapper.createBiographyRequest(
                title: title,
                firstName: firstName,
                lastName: lastName,
                phone: phone,
                email: email,
                street: street,
                city: city,
                country: country,
                position: position,
                employedFrom: employedFrom != nil ? employedFrom!.toBackendFormat() : "",
                user: u
            )
            onSubmit(request)
        } else {
            let request = biographyRequestWrapper.updateBiographyRequest(
                id: biography!.id,
                title: title,
                firstName: firstName,
                lastName: lastName,
                phone: phone,
                email: email,
                street: street,
                city: city,
                country: country,
                position: position,
                employedFrom: employedFrom != nil ? employedFrom!.toBackendFormat() : "",
                user: u
            )
            onSubmit(request)
        }
    }

    private var backButton: some View {
        Button(action: onClose) {
            Text("Zpět")
                .frame(maxWidth: .infinity)
                .padding()
                .background(Color.gray.opacity(0.2))
                .foregroundColor(.black)
                .cornerRadius(8)
        }
    }

    private func initializeForm() {
        guard let bio = biography else { return }
        title = bio.title ?? ""
        firstName = bio.firstName
        lastName = bio.lastName
        phone = bio.phone
        email = bio.email
        street = bio.street
        city = bio.city
        country = bio.country
        position = bio.position
        employedFrom = parseDate(from: bio.employedFrom)
    }

    private func parseDate(from string: String?) -> Date? {
        guard let string = string else { return nil }
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
