import SwiftUI
import shared

@main
struct iOSApp: App {
	init() {
        ModulesKt.doInitKoin() { _ in}
	}

	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
}
