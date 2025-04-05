import SwiftUI
import Shared

struct FooterView: View {
    @ObservedObject var tokenWrapper: TokenManagerWrapper

    var body: some View {
        HStack {
            VStack(alignment: .leading) {
                Text("Login: \(tokenWrapper.getAccount()?.login ?? "")")
                    .font(.system(size: 15))
                    .foregroundColor(.black)

                Text(tokenWrapper.getAccount()?.authorities.contains(Authority.roleAdmin) == true ? "Administrátor" : "Uživatel")
                    .font(.system(size: 13, weight: .bold))
                    .foregroundColor(.black)
            }

            Spacer()

            Button(action: {
                tokenWrapper.logout()
            }) {
                Text("Odhlásit")
                    .foregroundColor(.white)
                    .padding(.horizontal, 16)
                    .padding(.vertical, 8)
                    .background(Color.red)
                    .cornerRadius(8)
            }
        }
        .padding(12)
    }
}
