import React, { useContext } from 'react';
import { View, Text, StyleSheet } from 'react-native';
import { UsernameContext } from '../Contexte/contexte.js';

export default function HomeScreen() {
    const [username] = useContext(UsernameContext);

    return (
        <View style={styles.container}>
            <Text style={styles.welcomeText}>Bienvenue</Text>
            {username ? (
                <Text style={styles.usernameText}>Vous êtes connecté en tant que <Text style={styles.usernameHighlight}>{username}</Text></Text>
            ) : (
                <Text style={styles.errorText}>Aucun utilisateur connecté</Text>
            )}
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#ffffff',
        padding: 20,
    },
    welcomeText: {
        fontSize: 28,
        fontWeight: 'bold',
        marginBottom: 20,
    },
    usernameText: {
        fontSize: 20,
        textAlign: 'center',
    },
    usernameHighlight: {
        fontWeight: 'bold',
        color: '#0077ff',
    },
    errorText: {
        color: 'red',
        fontSize: 18,
        marginTop: 10,
        textAlign: 'center',
    },
});
