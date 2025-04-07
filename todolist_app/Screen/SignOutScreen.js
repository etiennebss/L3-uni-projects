import React from 'react';
import { View, TouchableOpacity, Text, StyleSheet } from 'react-native';
import { TokenContext, UsernameContext } from '../Contexte/contexte.js';

export default function SignOutScreen({ navigation }) {
    return (
        <TokenContext.Consumer>
            {([token, setToken]) => (
                <UsernameContext.Consumer>
                    {([username, setUsername]) => (
                        <View style={styles.container}>
                            <TouchableOpacity
                                style={styles.signOutButton}
                                onPress={() => {
                                    setToken(null);
                                    setUsername(null);
                                    navigation.navigate('Connexion');
                                }}
                            >
                                <Text style={styles.buttonText}>Se Déconnecter</Text>
                            </TouchableOpacity>
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
        backgroundColor: '#fff',
    },
    signOutButton: {
        backgroundColor: 'red',
        borderRadius: 1000,
        width: 200,
        height: 200,
        justifyContent: 'center',
        alignItems: 'center',
    },
    buttonText: {
        color: 'white',
        fontSize: 20,
        fontWeight: 'bold',
    },
});
