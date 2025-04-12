import SwiftUI
import Shared

struct LoginView: View {
    let tokenManagerWrapper: TokenManagerWrapper
    @Environment(\.presentationMode) var presentationMode
    @State private var username = ""
    @State private var password = ""
    @State private var error = ""
    
    var body: some View {
        VStack {
            Text("Curriculum").font(.system(size: 60))
            TextField("Uživatelské jméno", text: $username).padding(.vertical, 15)
            SecureField("Heslo", text: $password).padding(.vertical, 15)
            Button("Přihlásit") {
                authenticateUser()
            }
                .frame(maxWidth: .infinity)
                .padding()
                .background(Color.blue)
                .foregroundColor(.white)
                .cornerRadius(10)
            
            if (error != "") {
                Text(error).font(.system(size: 18)).foregroundColor(.red).padding()
            }
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
        .padding()
    }
    
    func authenticateUser() {
        let loginRequest = LoginRequest(username: username, password: password, rememberMe: false)
        tokenManagerWrapper.login(loginRequest: loginRequest) { success in
            if (success) {
                // Dismiss the login view and navigate to the main content
                presentationMode.wrappedValue.dismiss()
            } else {
                error = "Přihlášení selhalo. Zkontrolujte uživatelské jméno a heslo."
            }
        }
    }
}
