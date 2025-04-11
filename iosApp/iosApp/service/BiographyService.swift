import Foundation
import Shared

class BiographyService {
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

    func getBiography() async throws -> BiographyResponse {
        let (token, username) = try getAuthData()
        do {
            let biography = try await backendApi.getBiography(token: token, username: username)
            if (biography == nil) {
                throw BiographyServiceError.notFoundError
            } else {
                return biography!
            }
        } catch is KotlinIllegalStateException {
            tokenWrapper.logout()
            throw BiographyServiceError.authError
        }
    }

    func createBiography(_ request: BiographyRequest) async throws -> BiographyResponse {
        let (token, username) = try getAuthData()
        return try await handleRequest {
            try await backendApi.saveBiography(token: token, biographyRequest: request, biographyId: nil)
        }
    }

    func updateBiography(_ request: BiographyRequest) async throws -> BiographyResponse {
        let (token, username) = try getAuthData()
        return try await handleRequest {
            try await backendApi.saveBiography(token: token, biographyRequest: request, biographyId: request.id!)
        }
    }

    func getLanguagesByBiography(biographyId: Int) async throws -> [LanguageResponse] {
        let (token, _) = try getAuthData()
        return try await handleRequest {
            try await backendApi.getLanguagesByBiography(token: token, biographyId: Int32(biographyId))
        }
    }
    
    func createLanguage(request: LanguageRequest) async throws -> LanguageResponse {
        let (token, _) = try getAuthData()
        return try await handleRequest {
            try await backendApi.saveLanguage(token: token, languageRequest: request, languageId: nil)
        }
    }

    func updateLanguage(request: LanguageRequest) async throws -> LanguageResponse {
        let (token, _) = try getAuthData()
        return try await handleRequest {
            try await backendApi.saveLanguage(token: token, languageRequest: request, languageId: request.id)
        }
    }

    func deleteLanguage(languageId: Int) async throws -> Bool {
        let (token, _) = try getAuthData()
        return try await handleRequest {
            try await backendApi.deleteLanguage(token: token, languageId: Int32(languageId)).boolValue
        }
    }

    func getEducationsByBiography(biographyId: Int) async throws -> [EducationResponse] {
        let (token, _) = try getAuthData()
        return try await handleRequest {
            try await backendApi.getEducationsByBiography(token: token, biographyId: Int32(biographyId))
        }
    }
    
    func createEducation(request: EducationRequest) async throws -> EducationResponse {
        let (token, _) = try getAuthData()
        return try await handleRequest {
            try await backendApi.saveEducation(token: token, educationRequest: request, educationId: nil)
        }
    }

    func updateEducation(request: EducationRequest) async throws -> EducationResponse {
        let (token, _) = try getAuthData()
        return try await handleRequest {
            try await backendApi.saveEducation(token: token, educationRequest: request, educationId: request.id)
        }
    }

    func deleteEducation(educationId: Int) async throws -> Bool {
        let (token, _) = try getAuthData()
        return try await handleRequest {
            try await backendApi.deleteEducation(token: token, educationId: Int32(educationId)).boolValue
        }
    }


    func getProjectsByBiography(biographyId: Int) async throws -> [ProjectResponse] {
        let (token, _) = try getAuthData()
        return try await handleRequest {
            try await backendApi.getProjectsByBiography(token: token, biographyId: Int32(biographyId))
        }
    }

    func getSkillsByBiography(biographyId: Int) async throws -> [SkillResponse] {
        let (token, _) = try getAuthData()
        return try await handleRequest {
            try await backendApi.getSkillsByBiography(token: token, biographyId: Int32(biographyId))
        }
    }
}
