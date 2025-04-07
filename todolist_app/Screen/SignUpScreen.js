import React, { useState } from 'react';
import { StyleSheet, View, TextInput, Text, TouchableOpacity } from 'react-native';
import { TokenContext, UsernameContext } from '../Contexte/contexte.js';
import { signUp } from '../utils/sign.js';

export default function SignUpScreen({ navigation }) {
    const [username, setUsernameLocal] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState(null);

    const onSubmit = async (setToken, setUsername) => {
        try {
            const token = await signUp(username, password);
            setToken(token);
            setUsername(username);
            navigation.navigate('Accueil');
        } catch (err) {
            setError(err.message);
        }
    };

    return (
        <TokenContext.Consumer>
            {([token, setToken]) => (
                <UsernameContext.Consumer>
                    {([currentUsername, setUsername]) => (
                        <View style={styles.container}>
                            <Text style={styles.title}>Inscription</Text>
                            <TextInput
                                style={styles.textInput}
                                placeholder="Nom d'utilisateur"
                                value={username}
                                onChangeText={setUsernameLocal}
                                placeholderTextColor="#aaa"
                            />
                            <TextInput
                                style={styles.textInput}
                                placeholder="Mot de passe"
                                secureTextEntry={true}
                                value={password}
                                onChangeText={setPassword}
                                placeholderTextColor="#aaa"
                            />
                            <TouchableOpacity style={styles.button} onPress={() => onSubmit(setToken, setUsername)}>
                                <Text style={styles.buttonText}>S'inscrire</Text>
                            </TouchableOpacity>
                            {error && <Text style={styles.errorText}>{error}</Text>}
                        </View>
                    )}
                </UsernameContext.Consumer>
            )}
        </TokenContext.Consumer>
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
    title: {
        fontSize: 24,
        fontWeight: 'bold',
        marginBottom: 20,
        color: '#333',
    },
    textInput: {
        height: 50,
        width: '90%',
        backgroundColor: '#fff',
        borderRadius: 25,
        paddingHorizontal: 20,
        marginVertical: 10,
        borderWidth: 1,
        fontSize: 16,
        color: '#333',
    },
    button: {
        backgroundColor: '#0077ff',
        paddingVertical: 15,
        paddingHorizontal: 40,
        borderRadius: 25,
        alignItems: 'center',
        marginTop: 20,
        width: '90%',
    },
    buttonText: {
        color: '#fff',
        fontSize: 18,
        fontWeight: '600',
    },
    errorText: {
        color: 'red',
        marginTop: 10,
        fontSize: 14,
    },
});
