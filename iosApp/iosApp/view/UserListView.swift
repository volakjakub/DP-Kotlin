import SwiftUI
import Shared

struct UserListView: View {
    let biographyService: BiographyService
    let userService: UserService
    let account: AccountResponse

    @State private var page: Int = 0
    @State private var size: Int = 5
    @State private var canLoadNext: Bool = true
    @State private var isLoadingAccounts = false
    @State private var error: String?
    @State private var showBio = false
    @State private var showAccountForm = false
    @State private var accounts: [AccountResponse] = []
    @State private var accountEdit: AccountResponse?
    @State private var accountBio: AccountResponse?

    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            headerContent
            bodyContent
        }
        .onAppear(perform: loadUsers)
        .onChange(of: accountEdit) { newValue in
            showAccountForm = newValue != nil
        }
        .onChange(of: accountBio) { newValue in
            showBio = newValue != nil
        }
        .sheet(isPresented: $showAccountForm) {
            accountFormSheet
        }
        .sheet(isPresented: $showBio) {
            bioSheet
        }
    }
    
    private var headerContent: some View {
        VStack {
            Text("Uživatelé")
                .font(.system(size: 20, weight: .bold))
                .foregroundColor(.black)
                .padding(.top, 15)
                .padding(.bottom, 10)
            
            Button(action: {
                accountEdit = nil
                showAccountForm = true
            }) {
                Text("Vytvořit")
                    .foregroundColor(.white)
                    .padding()
                    .frame(maxWidth: .infinity)
                    .background(Color.blue)
                    .cornerRadius(8)
            }
            
            if let error = error {
                Text(error)
                    .foregroundColor(.red)
                    .font(.footnote)
                    .frame(maxWidth: .infinity, alignment: .center)
            }
            
            if isLoadingAccounts {
                ProgressView()
                    .progressViewStyle(CircularProgressViewStyle(tint: .blue))
                    .frame(maxWidth: .infinity, alignment: .center)
            }
        }
    }
    
    private var bodyContent: some View {
        VStack {
            if !accounts.isEmpty {
                ForEach(Array(accounts.enumerated()), id: \.offset) { index, user in
                    UserBox(account: user, accountEdit: $accountEdit, accountBio: $accountBio, admin: account)

                    if index < accounts.count - 1 {
                        Divider()
                            .background(Color.gray)
                            .padding(.vertical, 5)
                    }
                }
            }
            
            if (canLoadNext) {
                Button(action: loadNext) {
                    Text("Další")
                        .foregroundColor(.white)
                        .padding()
                        .frame(maxWidth: .infinity)
                        .background(Color.blue)
                        .cornerRadius(8)
                }
            }
        }
    }

    private var accountFormSheet: some View {
        UserFormDialog(
            isVisible: $showAccountForm,
            onSubmit: onFormSubmit,
            onDismiss: {
                accountEdit = nil
                showAccountForm = false
            },
            existingAccount: accountEdit
        )
    }

    private var bioSheet: some View {
        VStack {
            VStack(spacing: 16) {
                Button(action: {
                    accountBio = nil
                    showBio = false
                }) {
                    Text("Zavřít")
                        .padding()
                        .frame(maxWidth: .infinity)
                        .background(Color.gray.opacity(0.2))
                        .cornerRadius(8)
                }
                .padding()
            }
            BiographyDetailView(biographyService: biographyService, account: accountBio!, canEdit: false)
        }
    }
    
    private func loadUsers() {
        isLoadingAccounts = true
        Task {
            do {
                let loadedAccounts = try await userService.getUsers(page: self.page, size: self.size)
                self.accounts = loadedAccounts
            } catch {
                self.error = error.localizedDescription
            }
            isLoadingAccounts = false
        }
    }
    
    private func loadNext() {
        isLoadingAccounts = true
        Task {
            do {
                let loadedAccounts = try await userService.getUsers(page: self.page + 1, size: self.size)
                self.accounts.append(contentsOf: loadedAccounts)
                if (loadedAccounts.isEmpty) {
                    canLoadNext = false
                }
            } catch {
                self.error = error.localizedDescription
            }
            self.page = self.page + 1
            isLoadingAccounts = false
        }
    }

    private func onFormSubmit(request: AccountRequest) {
        isLoadingAccounts = true
        Task {
            do {
                if request.id == nil {
                    let created = try await userService.createUser(request)
                    self.accounts.append(created)
                } else {
                    let updated = try await userService.updateUser(request)
                    if let index = self.accounts.firstIndex(where: { $0.id == updated.id }) {
                        self.accounts[index] = updated
                    }
                }
            } catch {
                self.error = error.localizedDescription
            }
            isLoadingAccounts = false
        }
    }
}
