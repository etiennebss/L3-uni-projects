import React, { useEffect, useState, useContext } from 'react';
import { View, FlatList, Text, StyleSheet, Button, Alert, CheckBox } from 'react-native';
import { getTodos, createTodo, deleteTodo, updateTodo } from '../utils/todo.js';
import { TokenContext } from '../Contexte/contexte.js';
import Input from '../utils/Input.js';

export default function TodoItemsScreen({ route }) {
  const { todoListId } = route.params;
  const [token] = useContext(TokenContext);
  const [todos, setTodos] = useState([]);
  const [newTodo, setNewTodo] = useState("");
  const [filter, setFilter] = useState("all");

  useEffect(() => {
    if (todoListId) {
      fetchTodos();
    } else {
      console.error('todoListId is undefined');
    }
  }, [todoListId]);

  const fetchTodos = async () => {
    try {
      const items = await getTodos(todoListId, token);
      setTodos(items);
    } catch (error) {
      console.error('Erreur lors du chargement des items:', error.message);
    }
  };

  const handleCreateTodo = async (todoContent) => {
    if (!todoContent.trim()) {
      Alert.alert('Erreur', 'Le contenu de la tâche ne peut pas être vide.');
      return;
    }

    try {
      const createdTodo = await createTodo(todoContent, todoListId, token);
      setTodos([...todos, createdTodo]);
      setNewTodo(""); 
    } catch (error) {
      console.error('Erreur lors de la création du todo:', error.message);
      Alert.alert('Erreur', 'Impossible de créer la tâche.');
    }
  };

  const handleDelete = async (id) => {
    try {
      await deleteTodo(id, token);
      Alert.alert('Succès', 'La tâche a été supprimée.');
      setTodos((prevTodos) => prevTodos.filter((todo) => todo.id !== id));
    } catch (error) {
      console.error('Erreur lors de la suppression:', error.message);
      Alert.alert('Erreur', "Impossible de supprimer cette tâche.");
    }
  };

  const toggleTodo = async (id, done) => {
    try {
      await updateTodo(id, !done, token);
      setTodos((prevTodos) =>
        prevTodos.map((todo) => (todo.id === id ? { ...todo, done: !done } : todo))
      );
    } catch (error) {
      console.error('Erreur lors de la mise à jour:', error.message);
    }
  };

  const toggleAllTodos = async (done) => {
    try {
      await Promise.all(
        todos.map((todo) =>
          updateTodo(todo.id, done, token)
        )
      );
      setTodos((prevTodos) =>
        prevTodos.map((todo) => ({ ...todo, done }))
      );
    } catch (error) {
      console.error('Erreur lors de la mise à jour de tous les todos:', error.message);
    }
  };

  const renderTodoItem = ({ item }) => (
    <View style={styles.todoItem}>
      <CheckBox
        value={item.done}
        onValueChange={() => toggleTodo(item.id, item.done)}
        style={styles.checkbox}
      />
      <Text style={[styles.todoContent, item.done && styles.todoDone]}>
        {item.content}
      </Text>
      <Button title="🗑️" onPress={() => handleDelete(item.id)} color="white" />
    </View>
  );

  // Filtrage des tâches
  const filteredTodos = todos.filter((todo) => {
    if (filter === "completed") {
      return todo.done === true;
    }
    if (filter === "incomplete") {
      return todo.done === false;
    }
    return true; // Par défaut, afficher toutes les tâches
  });

  // Calculer la progression (en pourcentage)
  const totalTodos = filteredTodos.length;
  const completedTodos = filteredTodos.filter((todo) => todo.done).length;
  const progress = totalTodos ? (completedTodos / totalTodos) : 1; // Progress entre 0 et 1

  return (
    <View style={styles.container}>
      <Input
        placeholder="Nouvelle tâche"
        value={newTodo}
        onChangeText={setNewTodo}
        onSubmit={handleCreateTodo} />
      
      {/* Boutons "Tout cocher" et "Tout décocher" */}
      <View style={styles.buttonContainer}>
        <View style={styles.buttonWrapper}>
          <Button
            title="Tout cocher"
            onPress={() => toggleAllTodos(true)}
          />
        </View>
        <View style={styles.buttonWrapper}>
          <Button
            title="Tout décocher"
            onPress={() => toggleAllTodos(false)}
          />
        </View>
      </View>

      {/* Boutons de filtrage */}
      <View style={styles.buttonContainer}>
        <View style={styles.buttonWrapper}>
          <Button
            title="Afficher tout"
            onPress={() => setFilter("all")}
          />
        </View>
        <View style={styles.buttonWrapper}>
          <Button
            title="Afficher cochés"
            onPress={() => setFilter("completed")}
          />
        </View>
        <View style={styles.buttonWrapper}>
          <Button
            title="Afficher non cochés"
            onPress={() => setFilter("incomplete")}
          />
        </View>
      </View>

      {/* Barre de progression */}
      <View style={styles.progressContainer}>
        <Text style={styles.progressText}>Progrès : {Math.round(progress * 100)}%</Text>
        <View style={styles.progressBar}>
          <View
            style={[
              styles.progressFill,
              { width: `${progress * 100}%` }, // Largeur dynamique en fonction du progrès
            ]}
          />
        </View>
      </View>

      <FlatList
        data={filteredTodos}
        keyExtractor={(item) => item.id.toString()}
        renderItem={renderTodoItem}
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding : 15,
    backgroundColor: '#fff', 
  },
  todoItem: {
    flexDirection: 'row',
    alignItems: 'center',
    padding: 15,
    margin : 8,
    backgroundColor: '#f9f9f9',
    borderRadius: 10, 
    borderWidth: 1,
    borderColor: '#ccc',
  },
  todoContent: {
    fontSize: 16,
    flex: 1,
    fontWeight: 'bold',
  },
  todoDone: {
    textDecorationLine: 'line-through',
    color: 'gray',
  },
  checkbox: {
    marginRight: 10,
  },
  buttonContainer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginVertical: 10,
  },
  buttonWrapper: {
    flex: 1,
    marginHorizontal: 5,
    borderRadius: 8,
    overflow: 'hidden', // Empêche les débordements
  },
  progressContainer: {
    marginVertical: 20,
    alignItems: 'center',
  },
  progressText: {
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 10,
  },
  progressBar: {
    width: '100%',
    height: 20,
    backgroundColor: 'lightgrey',
    borderRadius: 10,
    overflow: 'hidden', 
  },
  progressFill: {
    height: '100%',
    backgroundColor: 'green', 
  },
});
