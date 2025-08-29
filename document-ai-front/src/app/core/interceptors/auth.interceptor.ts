import { inject } from "@angular/core"
import type { HttpInterceptorFn } from "@angular/common/http"
import { AuthService } from "../../features/auth/services/auth.service"

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService)
  const token = authService.getToken()

  // Skip adding token for login requests
  if (req.url.includes("/auth/login")) {
    console.log(`%c🔓 SKIPPING TOKEN`, "color: #FF9800; font-weight: bold")
    console.log(`   URL: ${req.url}`)
    console.log(`   Method: ${req.method}`)
    console.log("---")
    return next(req)
  }

  // If we have a token, clone the request and add the Authorization header
  if (token) {
    const authReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`,
      },
    })

    console.log(`%c🔐 TOKEN ADDED TO REQUEST`, "color: #4CAF50; font-weight: bold")
    console.log(`   URL: ${req.url}`)
    console.log(`   Method: ${req.method}`)
    console.log(`   Token (first 30 chars): ${token.substring(0, 30)}...`)
    console.log(`   Authorization Header: Bearer ${token.substring(0, 20)}...`)

    // Decode and show token info
    try {
      const payload = JSON.parse(atob(token.split(".")[1]))
      console.log(`   User: ${payload.sub || "N/A"}`)
      console.log(`   Role: ${payload.role || "N/A"}`)

      // Check expiration
      const exp = payload.exp * 1000
      const now = Date.now()
      const timeLeft = exp - now

      if (timeLeft > 0) {
        console.log(`   ⏰ Expires in: ${Math.round(timeLeft / 1000 / 60)} minutes`)
      } else {
        console.log(`   ⚠️ TOKEN IS EXPIRED!`)
      }
    } catch (error) {
      console.log(`   ❌ Could not decode token payload`)
    }

    console.log("---")
    return next(authReq)
  }

  console.log(`%c❌ NO TOKEN FOUND`, "color: #F44336; font-weight: bold")
  console.log(`   URL: ${req.url}`)
  console.log(`   Method: ${req.method}`)
  console.log(`   Proceeding without Authorization header`)
  console.log("---")
  return next(req)
}
