import SwiftUI
import Shared

struct UserBox: View {
    var account: AccountResponse
    @Binding var accountEdit: AccountResponse?
    @Binding var accountBio: AccountResponse?
    var admin: AccountResponse
    
    @State private var isExpanded = false
    
    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            HStack {
                VStack(alignment: .leading, spacing: 4) {
                    Text(account.firstName + " " + account.lastName)
                        .font(.system(size: 14, weight: .bold))
                        .foregroundColor(.black)
                    
                    Text("Login: " + account.login)
                        .font(.system(size: 14, weight: .regular))
                        .foregroundColor(.black)
                    
                    if (account.activated) {
                        Text("Status: aktivní")
                            .font(.system(size: 14, weight: .regular))
                            .foregroundColor(.black)
                    } else {
                        Text("Status: neaktivní")
                            .font(.system(size: 14, weight: .regular))
                            .foregroundColor(.black)
                    }
                    Text("Role: " + account.authorities.map { transalateAuthorities(authority: $0) }.joined(separator: ", "))
                        .font(.system(size: 13, weight: .regular))
                        .foregroundColor(.black)
                }
                Spacer()
            }
            .padding(7)
            .contentShape(Rectangle())
            .onTapGesture {
                withAnimation(.easeInOut(duration: 0.3)) {
                    isExpanded.toggle()
                }
            }

            if isExpanded {
                Divider()
                    .background(Color.black)
                    .padding(.vertical, 5)

                HStack {
                    Spacer()
                    if (account.authorities.contains(Authority.roleAdmin) != true) {
                        Button(action: {
                            accountBio = account
                        }) {
                            Text("Životopis")
                                .foregroundColor(.white)
                        }
                        .buttonStyle(.borderedProminent)
                        .tint(.blue)
                    }

                    if (admin.login != account.login) {
                        Button(action: {
                            accountEdit = account
                        }) {
                            Text("Upravit")
                                .foregroundColor(.white)
                        }
                        .buttonStyle(.borderedProminent)
                        .tint(.blue)
                    }
                }
                .padding(7)
            }
        }
        .padding(8)
        .background(Color.gray.opacity(0.2))
        .cornerRadius(8)
    }
    
    private func transalateAuthorities(authority: Authority) -> String {
        if (authority == Authority.roleAdmin) {
            return "Administrátor"
        }
        if (authority == Authority.roleUser) {
            return "Uživatel"
        }
        return "-"
    }
}
