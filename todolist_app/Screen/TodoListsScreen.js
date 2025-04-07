import React, { useState, useEffect, useContext } from 'react';
import { View, FlatList, Text, StyleSheet, Button, Alert } from 'react-native';
import { getTodoLists, deleteTodoList, createTodoList } from '../utils/todoList.js';
import Input from '../utils/Input.js';
import { TokenContext, UsernameContext } from '../Contexte/contexte.js';

export default function TodoListsScreen({ navigation }) {
  const [todoLists, setTodoLists] = useState([]);
  const [newListTitle, setNewListTitle] = useState(""); 

  const [token] = useContext(TokenContext);
  const [username] = useContext(UsernameContext);

  useEffect(() => {
    fetchTodoLists();
  }, []);

  const fetchTodoLists = async () => {
    try {
      const lists = await getTodoLists(username, token);
      setTodoLists(lists);
    } catch (error) {
      console.error("Erreur lors du chargement des TodoLists:", error.message);
    }
  };

  const handleDelete = async (id) => {
    try {
      await deleteTodoList(id, token);
      Alert.alert("Succès", "La liste a été supprimée.");
      setTodoLists((prevLists) => prevLists.filter((list) => list.id !== id));
    } catch (error) {
      Alert.alert("Erreur", "Impossible de supprimer cette liste.");
    }
  };

  const handleCreate = async (title) => {
    if (!title.trim()) {
      Alert.alert('Erreur', 'Le titre de la liste ne peut pas être vide.');
      return;
    }

    try {
      const newList = await createTodoList(username, title, token);
      setTodoLists([...todoLists, newList]);
      setNewListTitle("");
    } catch (error) {
      Alert.alert('Erreur', 'Impossible de créer la liste.');
    }
  };

  const renderTodoList = ({ item }) => (
    <View style={styles.todoListItem}>
      <Text style={styles.todoListTitle}>{item.title}</Text>
      <View style={styles.buttonContainer}>
        <View style={styles.roundedButton}>
          <Button
            title="Accéder"
            onPress={() => navigation.navigate('Items', { todoListId: item.id })}
          />
        </View>
        <View style={styles.roundedButton}>
          <Button
            title="🗑️"
            onPress={() => handleDelete(item.id)}
          color={'white'}/>
        </View>
      </View>
    </View>
  );

  return (
    <View style={styles.container}>
      <Input
        placeholder="Nouvelle liste"
        value={newListTitle}
        onChangeText={setNewListTitle}
        onSubmit={handleCreate}
      />
      <FlatList
        data={todoLists}
        keyExtractor={(item) => item.id.toString()}
        renderItem={renderTodoList}
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 15,
    backgroundColor: '#fff',
  },
  todoListItem: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    padding: 15,
    margin: 8, 
    backgroundColor: '#f9f9f9',
    borderRadius: 10, 
    borderWidth: 1,
  },
  todoListTitle: {
    fontSize: 16,
    flex: 1,
    fontWeight: 'bold',
  },
  buttonContainer: {
    flexDirection: 'row',
    gap: 8, // Espacement entre les boutons
  },
  roundedButton: {
    overflow: 'hidden', // Empêche les débordements
    borderRadius: 8,
  },
});
