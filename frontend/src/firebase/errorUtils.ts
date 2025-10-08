// Utility function to convert Firebase error codes to user-friendly messages
export function getFirebaseErrorMessage(error: any): string {
  const errorCode = error?.code || error?.message || 'Unknown error'

  switch (errorCode) {
    // Authentication errors
    case 'auth/invalid-credential':
    case 'auth/user-not-found':
    case 'auth/wrong-password':
    case 'auth/invalid-email':
      return 'Invalid Email or Password.'

    case 'auth/email-already-in-use':
      return 'An account with this email already exists. Please sign in instead.'

    case 'auth/weak-password':
      return 'Password is too weak. Please use at least 6 characters.'

    case 'auth/invalid-password':
      return 'Invalid password. Please try again.'

    case 'auth/user-disabled':
      return 'This account has been disabled. Please contact support.'

    case 'auth/too-many-requests':
      return 'Too many failed attempts. Please try again later.'

    case 'auth/network-request-failed':
      return 'Network error. Please check your internet connection and try again.'

    case 'auth/popup-closed-by-user':
      return 'Sign-in was cancelled. Please try again.'

    case 'auth/popup-blocked':
      return 'Pop-up was blocked by your browser. Please allow pop-ups and try again.'

    case 'auth/cancelled-popup-request':
      return 'Sign-in request was cancelled. Please try again.'

    case 'auth/account-exists-with-different-credential':
      return 'An account already exists with the same email but different sign-in credentials.'

    case 'auth/credential-already-in-use':
      return 'This credential is already associated with a different user account.'

    case 'auth/requires-recent-login':
      return 'This operation is sensitive and requires recent authentication. Please log in again.'

    case 'auth/operation-not-allowed':
      return 'This sign-in method is not enabled. Please contact support.'

    case 'auth/timeout':
      return 'The operation has timed out. Please try again.'

    // Password reset errors
    case 'auth/missing-email':
      return 'Please enter your email address.'

    case 'auth/invalid-action-code':
      return 'The password reset link is invalid or has expired.'

    case 'auth/expired-action-code':
      return 'The password reset link has expired. Please request a new one.'

    case 'auth/user-token-expired':
      return 'Your session has expired. Please sign in again.'

    case 'auth/invalid-user-token':
      return 'Invalid session. Please sign in again.'

    // Default fallback
    default:
      // If it's already a user-friendly message, return it
      if (error?.message && !error.message.includes('Firebase:')) {
        return error.message
      }
      // Otherwise, return a generic message
      return 'An unexpected error occurred. Please try again or contact support.'
  }
}