import SwiftUI
import Shared

struct ContentView: View {
    @ObservedObject var tokenWrapper: TokenManagerWrapper
    let backendApi: BackendApi
    let account: AccountResponse?
    let biographyService: BiographyService
    let userService: UserService

    init(tokenWrapper: TokenManagerWrapper, backendApi: BackendApi) {
        self.tokenWrapper = tokenWrapper
        self.account = tokenWrapper.getAccount()
        self.backendApi = backendApi
        self.biographyService = BiographyService(tokenWrapper: tokenWrapper, backendApi: backendApi)
        self.userService = UserService(tokenWrapper: tokenWrapper, backendApi: backendApi)
    }

    var body: some View {
        ZStack(alignment: .bottom) {
            ScrollView {
                VStack(alignment: .center) {
                    Text("Curriculum")
                        .font(.largeTitle)
                        .foregroundColor(.black)
                        .multilineTextAlignment(.center)
                        .padding(.top, 30)
                        .padding(.bottom, 20)
                        .frame(maxWidth: .infinity)

                    if (account != nil) {
                        if tokenWrapper.getAccount()?.authorities.contains(Authority.roleAdmin) == true {
                            VStack {
                                ScrollView {
                                    VStack(spacing: 16) {
                                        UserListView(biographyService: biographyService, userService: userService, account: account!)
                                    }
                                    .padding()
                                }
                            }
                        } else {
                            BiographyDetailView(biographyService: biographyService, account: account!, canEdit: true)
                        }
                    }
                }
                .padding(.bottom, 80) // Space for footer
            }

            // Footer
            VStack {
                Spacer()
                FooterView(tokenWrapper: tokenWrapper)
                    .frame(height: 75)
                    .frame(maxWidth: .infinity)
                    .background(Color.gray)
            }
        }
        .ignoresSafeArea(edges: .bottom)
    }
}
