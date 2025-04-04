import SwiftUI
import Shared

@main
struct iOSApp: App {
    @StateObject var tokenWrapper: TokenManagerWrapper

    init() {
        let base = "http://192.168.0.101:8080/api"
        let tokenManagerProvider = TokenManagerProvider()
        let tokenManager = tokenManagerProvider.createTokenManager()
        tokenManager.clearToken()
        _tokenWrapper = StateObject(wrappedValue: TokenManagerWrapper(tokenManager: tokenManager, backendApi: BackendApi(baseUrl: base)))
    }
    var body: some Scene {
        WindowGroup {
            if tokenWrapper.refresh && tokenWrapper.getToken() != nil {
                ContentView(backendApi: BackendApi(baseUrl: "http://192.168.0.101:8080/api"))
            } else {
                LoginView(tokenManagerWrapper: tokenWrapper)
            }
        }
    }
}
