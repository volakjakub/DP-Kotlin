import SwiftUI
import Shared

struct UserFormDialog: View {
    @Binding var isVisible: Bool
    var onSubmit: (AccountRequest) -> Void
    var onDismiss: () -> Void
    var existingAccount: AccountResponse?

    @State private var selectedLogin: String = ""
    @State private var selectedFirstName: String = ""
    @State private var selectedLastName: String = ""
    @State private var selectedEmail: String = ""
    @State private var selectedActivated: Bool = true
    @State private var selectedAuthorities: [Int: Bool] = [0: false, 1: false]

    var body: some View {
        if isVisible {
            ZStack {
                Color.white.edgesIgnoringSafeArea(.all)
                VStack(spacing: 8) {
                    Text("Přidat uživatele")
                        .font(.headline)
                    
                    formContent
                    buttonContent
                }
                .padding()
                .background(Color.white)
                .cornerRadius(12)
                .onAppear {
                    selectedLogin = existingAccount?.login ?? ""
                    selectedFirstName = existingAccount?.firstName ?? ""
                    selectedLastName = existingAccount?.lastName ?? ""
                    selectedEmail = existingAccount?.email ?? ""
                    selectedActivated = existingAccount?.activated ?? true

                    selectedAuthorities[0] = existingAccount?.authorities.contains(where: { $0.displayName == Authority.roleAdmin.displayName }) == true
                    selectedAuthorities[1] = existingAccount?.authorities.contains(where: { $0.displayName == Authority.roleUser.displayName }) == true
                }
            }
        }
    }
    
    private var formContent: some View {
        Group {
            TextField("Login:", text: $selectedLogin)
                .textFieldStyle(.roundedBorder)

            TextField("Jméno:", text: $selectedFirstName)
                .textFieldStyle(.roundedBorder)

            TextField("Příjmení:", text: $selectedLastName)
                .textFieldStyle(.roundedBorder)

            TextField("Email:", text: $selectedEmail)
                .textFieldStyle(.roundedBorder)

            Text("Status:")
                .font(.headline)
                .padding(.top, 8)

            Toggle("Je uživatel aktivní?", isOn: $selectedActivated)

            Text("Uživatelské role:")
                .font(.headline)
                .padding(.top, 8)

            Toggle("Administrátor", isOn: Binding(
                get: { selectedAuthorities[0] ?? false },
                set: { selectedAuthorities[0] = $0 }
            ))

            Toggle("Uživatel", isOn: Binding(
                get: { selectedAuthorities[1] ?? false },
                set: { selectedAuthorities[1] = $0 }
            ))
        }
        .textFieldStyle(RoundedBorderTextFieldStyle())
    }

    private var buttonContent: some View {
        HStack {
            Spacer()
            if isFormValid {
                Button(action: handleSubmit) {
                    Text("Uložit")
                        .padding()
                        .background(Color.blue)
                        .foregroundColor(.white)
                        .cornerRadius(8)
                }
            }

            Button(action: onDismiss) {
                Text("Zpět")
                    .padding()
                    .background(Color.gray.opacity(0.2))
                    .cornerRadius(8)
            }
        }
    }

    private var isFormValid: Bool {
        !selectedLogin.isEmpty && !selectedFirstName.isEmpty &&
        !selectedLastName.isEmpty && !selectedEmail.isEmpty
    }
    
    private func handleSubmit() {
        let accountRequestWrapper: AccountRequestWrapper = AccountRequestWrapper()
        var auths: [Authority] = []
        if selectedAuthorities[0] == true {
            auths.append(.roleAdmin)
        }
        if selectedAuthorities[1] == true {
            auths.append(.roleUser)
        }

        if (existingAccount == nil) {
            let accountRequest = accountRequestWrapper.createAccountRequest(
                login: selectedLogin,
                firstName: selectedFirstName,
                lastName: selectedLastName,
                email: selectedEmail,
                activated: selectedActivated,
                langKey: "cs",
                authorities: auths
            )
            onSubmit(accountRequest)
        } else {
            let accountRequest = accountRequestWrapper.updateAccountRequest(
                id: existingAccount!.id,
                login: selectedLogin,
                firstName: selectedFirstName,
                lastName: selectedLastName,
                email: selectedEmail,
                activated: selectedActivated,
                langKey: "cs",
                authorities: auths
            )
            onSubmit(accountRequest)
        }
        onDismiss()
    }

}
