import SwiftUI
import Shared

@main
struct iOSApp: App {
    let tokenManager: TokenManager
    let backendApi: BackendApi

    init() {
        let baseUrl: String = "http://localhost:8080/api"
        let tokenManagerProvider = TokenManagerProvider()
        self.tokenManager = tokenManagerProvider.createTokenManager()
        self.tokenManager.clearToken()
        self.backendApi = BackendApi(baseUrl: baseUrl, tokenManager: self.tokenManager)
    }
    var body: some Scene {
        WindowGroup {
            ContentView(backendApi: self.backendApi)
        }
    }
}
