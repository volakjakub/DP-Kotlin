import SwiftUI
import Shared

struct ContentView: View {
    @ObservedObject var tokenWrapper: TokenManagerWrapper
    let backendApi: BackendApi
    let biographyService: BiographyService

    init(tokenWrapper: TokenManagerWrapper, backendApi: BackendApi) {
        self.tokenWrapper = tokenWrapper
        self.backendApi = backendApi
        self.biographyService = BiographyService(tokenWrapper: tokenWrapper, backendApi: backendApi)
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

                    if tokenWrapper.getAccount()?.authorities.contains(Authority.roleAdmin) == true {
                        // TODO: Admin view
                    } else {
                        BiographyDetailView(biographyService: biographyService)
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
