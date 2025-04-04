import SwiftUI
import Shared

@main
struct iOSApp: App {
    let tokenManager: TokenManager
    let backendApi: BackendApi

    init() {
        let baseUrl: ProcessInfo.processInfo.environment["BASE_URL"]
        self.tokenManager = TokenManagerProvider.createTokenManager()
        self.tokenManager.clearToken()
        self.backendApi = BackendApi(baseUrl: baseUrl, tokenManager: self.tokenManager)
    }
    var body: some Scene {
        WindowGroup {
            ContentView(backendApi: backendApi)
        }
    }
}