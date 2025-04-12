import Foundation
import Shared

class UserService {
    private let backendApi: BackendApi
    private let tokenWrapper: TokenManagerWrapper
    
    init(tokenWrapper: TokenManagerWrapper, backendApi: BackendApi) {
        self.tokenWrapper = tokenWrapper
        self.backendApi = backendApi
    }
    
    private func getAuthData() throws -> (token: String, username: String) {
        guard let token = tokenWrapper.getToken(),
              let username = tokenWrapper.getAccount()?.login else {
            tokenWrapper.logout()
            throw BiographyServiceError.authError
        }
        return (token, username)
    }
    
    private func handleRequest<T>(_ action: () async throws -> T?) async throws -> T {
        do {
            if let result = try await action() {
                return result
            } else {
                throw BiographyServiceError.loadingError
            }
        } catch {
            tokenWrapper.logout()
            throw error
        }
    }
    
    func getUsers(page: Int, size: Int) async throws -> [AccountResponse] {
        let (token, _) = try getAuthData()
        return try await handleRequest {
            try await backendApi.getUsers(token: token, page: Int32(page), size: Int32(size))
        }
    }
    
    func createUser(_ request: AccountRequest) async throws -> AccountResponse {
        let (token, _) = try getAuthData()
        return try await handleRequest {
            try await backendApi.saveUser(token: token, accountRequest: request, accountId: nil)
        }
    }

    func updateUser(_ request: AccountRequest) async throws -> AccountResponse {
        let (token, _) = try getAuthData()
        return try await handleRequest {
            try await backendApi.saveUser(token: token, accountRequest: request, accountId: request.id!)
        }
    }
}
