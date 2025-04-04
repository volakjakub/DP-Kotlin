import Foundation
import Shared

class TokenManagerWrapper: ObservableObject {
    private let backendApi: BackendApi
    private let tokenManager: TokenManager
    @Published var refresh: Bool

    init(tokenManager: TokenManager, backendApi: BackendApi) {
        self.backendApi = backendApi
        self.tokenManager = tokenManager
        self.refresh = false
    }
    
    func getToken() -> String? {
        return self.tokenManager.getToken()
    }

    func login(loginRequest: LoginRequest, completion: @escaping (Bool) -> Void) {
        backendApi.login(loginRequest: loginRequest) { [weak self] tokenString, error in
            DispatchQueue.main.async {
                if let t = tokenString {
                    self?.tokenManager.saveToken(token: t)
                    self?.refresh = true
                    completion(true)
                } else {
                    self?.refresh = false
                    completion(false)
                }
            }
        }
    }

    func logout() {
        self.refresh = false
        self.tokenManager.clearToken()
    }
}
