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

    func getAccount() -> AccountResponse? {
        return tokenManager.getAccount()
    }

    func login(loginRequest: LoginRequest, completion: @escaping (Bool) -> Void) {
        backendApi.login(loginRequest: loginRequest) { [weak self] tokenString, error in
            guard let self = self, let token = tokenString else {
                DispatchQueue.main.async {
                    self?.refresh = false
                    completion(false)
                }
                return
            }

            self.tokenManager.saveToken(token: token)

            self.backendApi.getAccount(token: token) { account, error in
                DispatchQueue.main.async {
                    if let account = account {
                        self.tokenManager.saveAccount(account: account)
                        self.refresh = true
                        completion(true)
                    } else {
                        self.refresh = false
                        completion(false)
                    }
                }
            }
        }
    }

    func logout() {
        self.refresh = false
        self.tokenManager.clearToken()
        tokenManager.clearAccount()
    }
}
