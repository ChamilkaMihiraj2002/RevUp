import {auth} from "./firebase";
import { signInWithEmailAndPassword,sendPasswordResetEmail, createUserWithEmailAndPassword,GoogleAuthProvider,signInWithPopup } from "firebase/auth";

export const signUp = (email: string, password: string) => {
    return createUserWithEmailAndPassword(auth, email, password);
}

export const logIn = (email: string, password: string) => {
    return signInWithEmailAndPassword(auth, email, password);
}

export const logInWithGoogle =async () => {
    const provider = new GoogleAuthProvider();
    const result = await signInWithPopup(auth, provider);
    return result
}

export const passwordReset = (email: string) => {
    return sendPasswordResetEmail(auth,email);};

export const logOut = () => {
    return auth.signOut();
};    