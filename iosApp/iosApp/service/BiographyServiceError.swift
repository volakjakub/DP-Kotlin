import Foundation

enum BiographyServiceError: Error, LocalizedError {
    case authError
    case loadingError
    case notFoundError

    var errorDescription: String? {
        switch self {
        case .authError:
            return "Chyba při načítání dat. Přihlaste se prosím znovu."
        case .loadingError:
            return "Chyba při načítání dat. Zkuste to prosím později."
        case .notFoundError:
            return "Chyba při načítání dat. Položka neexistuje."
        }
    }
}
