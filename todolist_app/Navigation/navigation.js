import React, { useContext } from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import { TokenContext } from '../Contexte/contexte.js';

import SignInScreen from '../Screen/SignInScreen.js';
import SignUpScreen from '../Screen/SignUpScreen.js';
import HomeScreen from '../Screen/HomeScreen.js';
import TodoListsScreen from '../Screen/TodoListsScreen.js';
import SignOutScreen from '../Screen/SignOutScreen.js';
import TodoItemsScreen from '../Screen/TodoItemsScreen.js';

const Tab = createBottomTabNavigator();

export default function Navigation() {
  const [token] = useContext(TokenContext);

  return (
    <NavigationContainer>
      {token == null ? (
        <Tab.Navigator>
          <Tab.Screen name="Connexion" component={SignInScreen} />
          <Tab.Screen name="Inscription" component={SignUpScreen} />
        </Tab.Navigator>
      ) : (
        <Tab.Navigator>
          <Tab.Screen name="Accueil" component={HomeScreen} />
          <Tab.Screen name="TodoLists" component={TodoListsScreen} />
          <Tab.Screen name="Items" component={TodoItemsScreen} options={{tabBarButton: () => null }}/>
          <Tab.Screen name="Déconnexion" component={SignOutScreen} />
        </Tab.Navigator>
      )}
    </NavigationContainer>
  );
}
