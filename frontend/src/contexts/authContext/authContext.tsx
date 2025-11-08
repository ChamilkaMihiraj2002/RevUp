import React, { useEffect, useState, useContext } from "react";
import type { ReactNode } from "react";
import { auth } from "../../firebase/firebase";
import { onAuthStateChanged } from "firebase/auth";
import type { User } from "firebase/auth";
import Cookies from "js-cookie";
import { getUserByFirebaseUID } from "../../services/userService";
import type { UserData } from "../../services/userService";
import { toast } from "sonner";

type AuthContextType = {
    currentUser: User | null;
    userLoggedIn: boolean;
    loading: boolean;
    userData: UserData | null;
    role: 'CUSTOMER' | 'TECHNICIAN' | null;
    accessToken: string | null;
    setUserData: (data: UserData | null) => void;
    setAccessToken: (token: string | null) => void;
    logout: () => Promise<void>;
}

const AuthContext = React.createContext<AuthContextType | undefined>(undefined);

export function useAuth(): AuthContextType {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error("useAuth must be used within an AuthProvider");
    }
    return context;
}

export function AuthProvider({ children }: { children: ReactNode }) {
    const [currentUser, setCurrentUser] = useState<User | null>(null);
    const [userLoggedIn, setUserLoggedIn] = useState(false);
    const [loading, setLoading] = useState(true);
    const [userData, setUserDataState] = useState<UserData | null>(null);
    const [role, setRole] = useState<'CUSTOMER' | 'TECHNICIAN' | null>(null);
    const [accessToken, setAccessTokenState] = useState<string | null>(null);

    useEffect(() => {
        const unsubscribe = onAuthStateChanged(auth, initializeUser);
        return unsubscribe;
    }, []);

    async function initializeUser(user: User | null) {
        if (user) {
            setCurrentUser(user);
            
            try {
                const token = await user.getIdToken();
                
                // Log the access token for Postman testing
                console.log('🔐 Firebase Access Token obtained:', token);
                console.log('📋 Copy this Bearer token for Postman:', `Bearer ${token}`);
                console.log('🔗 API Gateway URL for testing:', 'http://localhost:8080/api/v1');
                
                const cachedUserData = Cookies.get('userData');
                const cachedToken = Cookies.get('accessToken');
                
                if (cachedUserData && cachedToken) {
                    const parsedUserData = JSON.parse(cachedUserData);
                    setUserDataState(parsedUserData);
                    setRole(parsedUserData.role);
                    setAccessTokenState(token);
                    setUserLoggedIn(true);
                    
                    console.log('✅ Using cached user data and token');
                } else {
                    const dbUser = await getUserByFirebaseUID(user.uid, token);
                    setUserDataState(dbUser);
                    setRole(dbUser.role);
                    setAccessTokenState(token);
                    setUserLoggedIn(true);
                    
                    Cookies.set('userData', JSON.stringify(dbUser), { expires: 0.25 });
                    Cookies.set('accessToken', token, { expires: 0.25 });
                    
                    console.log('✅ Fresh user data loaded from database');
                }
                
                console.log('🎯 Token ready for API calls:', `Bearer ${token}`);
            } catch (error) {
                console.error("Error fetching user data:", error);
                setUserLoggedIn(false);
                setCurrentUser(null);
                toast.error("User not found in system. Please contact system assistance.");
                await auth.signOut();
            }
        } else {
            setCurrentUser(null);
            setUserLoggedIn(false);
            setUserDataState(null);
            setRole(null);
            setAccessTokenState(null);
            Cookies.remove('userData');
            Cookies.remove('accessToken');
        }
        setLoading(false);
    }

    const setUserData = (data: UserData | null) => {
        setUserDataState(data);
        if (data) {
            setRole(data.role);
            Cookies.set('userData', JSON.stringify(data), { expires: 0.25 });
        } else {
            setRole(null);
            Cookies.remove('userData');
        }
    };

    const setAccessToken = (token: string | null) => {
        setAccessTokenState(token);
        if (token) {
            Cookies.set('accessToken', token, { expires: 0.25 });
        } else {
            Cookies.remove('accessToken');
        }
    };

    const logout = async () => {
        await auth.signOut();
        setCurrentUser(null);
        setUserLoggedIn(false);
        setUserDataState(null);
        setRole(null);
        setAccessTokenState(null);
        Cookies.remove('userData');
        Cookies.remove('accessToken');
    };

    const value: AuthContextType = {
        currentUser,
        userLoggedIn,
        loading,
        userData,
        role,
        accessToken,
        setUserData,
        setAccessToken,
        logout
    };

    return (
        <AuthContext.Provider value={value}>
            {!loading && children}
        </AuthContext.Provider>
    );
}