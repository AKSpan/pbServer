package dbAPI;

import Entities.Contact;
import Entities.UsersEntity;
import com.mongodb.*;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Mongo {

    /*************************/
    private MongoCredential credential;
    private MongoClient mongoClient;
    private Datastore ds;
    /*************************/
    /**
     * Инициализация коннекта к БД
     */
    public void initMongoConnect() {
        try {
            Properties prop = new Properties();
            prop.load(getClass().getClassLoader().getResourceAsStream("database.properties"));

            if (credential == null)
                credential = MongoCredential.createCredential(prop.getProperty("username"), prop.getProperty("database"), prop.getProperty("password").toCharArray());
            if (mongoClient == null)
                mongoClient = new MongoClient(new ServerAddress(), Collections.singletonList(credential));
            if (ds == null)
                ds = new Morphia().createDatastore(mongoClient, prop.getProperty("database"));

        } catch (FileNotFoundException ex) {
            System.out.println("File not found");
            ex.getStackTrace();
        } catch (IOException ex) {
            System.err.println("Exception");
            ex.getStackTrace();
        } catch (NullPointerException ex) {
            System.err.println("NullP");
            ex.getStackTrace();
        }
    }


    /**
     * Получение всех данных
     *
     * @param clazz сущность (ex: new Contact())
     * @return {@link List}<{@link Object}>
     */
    public List<?> find(Object clazz) {
        initException();
        if (clazz.getClass().getSimpleName().equals(Contact.class.getSimpleName()))
            return ds.find(clazz.getClass()).order("surname").asList();
        return ds.find(clazz.getClass()).asList();
    }

    /**
     * Поиск данных по полю
     *
     * @param clazz тип сущности (Объект класса (ех. new Contact()))
     * @param field Поле по которому необх. искать
     * @param value Значение поля
     * @return {@link List}<{@link Contact}>
     */
    public List<?> find(Object clazz, String field, String value) {
        initException();
        if (clazz.getClass().getSimpleName().equals(Contact.class.getSimpleName())) {
            return ds.find(clazz.getClass()).order("surname").field(field).contains(value).asList();
        }
        return ds.find(clazz.getClass()).field(field).contains(value).asList();
    }


    /**
     * Вставка данных в бд
     *
     * @param record Объект для записи в бд
     */
    public void save(Object record) throws IllegalArgumentException {
        initException();
        try {
            ds.save(record);
        } catch (DuplicateKeyException ex) {
            System.err.println("Запись " + record.toString() + " дублирует данные");
            if (record.getClass().getSimpleName().equals(UsersEntity.class.getSimpleName()))
                throw new IllegalArgumentException("Username already exists.");
            if (record.getClass().getSimpleName().equals(Contact.class.getSimpleName()))
                throw new IllegalArgumentException("Cannot save record. Recording with this data already exists.");
        }
    }

    /**
     * Обновление поля записи
     *
     * @param record Обновляемая запись
     * @param field  Поле
     * @param value  значение
     */
    public void update(Object record, String field, String value) throws Exception {
        initException();
        try {
            UpdateOperations updtOp = ds.createUpdateOperations(record.getClass());
            ds.update(record, updtOp.set(field, value));
        } catch (DuplicateKeyException ex) {
            System.err.println("Обновляемые данные дублируют другие документы.");
            throw new Exception("Cannot update record. Updating duplicate records.");
        }
    }

    /**
     * Обновление нескольких полей записи
     *
     * @param record Обновляемая запись
     * @param updMap Обновляемые поля и их значения
     */
    public void update(Object record, Map updMap) throws Exception {
        initException();
        Iterator it = updMap.entrySet().iterator();
        UpdateOperations updOP = ds.createUpdateOperations(record.getClass());
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            //System.out.println(pair.getKey() + " = " + pair.getValue());
            updOP.set(pair.getKey() + "", pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }
        try {
            ds.update(record, updOP);
        } catch (DuplicateKeyException ex) {
            System.err.println("Обновляемые данные дублируют другие документы.");
            throw new Exception("Cannot update record. Updating duplicate records.");
        }
    }

    /**
     * Удаление записи(ей)
     *
     * @param field Поле для критерия
     * @param value Значение
     */
    public void delete(String field, String value) {
        initException();
        ds.delete(ds.createQuery(Contact.class).filter(field, value));
    }

    public void delete(Map delMap) {
        initException();
        Iterator it = delMap.entrySet().iterator();
        Query<Contact> query = ds.createQuery(Contact.class);
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            query.filter(pair.getKey() + "", pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }

        ds.delete(query);
    }


    /**
     * Исключение с информацией о том, что объект БД не инициализирован.
     * Использовать перед работой с данными.
     */
    private void initException() {
        if (ds == null)
            throw new NullPointerException("Объект БД не инициализирован. Выполните метод 'initMongoConnect()' до работы с данными.");
    }
}
