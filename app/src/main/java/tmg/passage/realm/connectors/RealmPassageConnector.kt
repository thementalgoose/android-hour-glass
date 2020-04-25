package tmg.passage.realm.connectors

import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.coroutines.flow.Flow
import tmg.passage.data.connectors.PassageConnector
import tmg.passage.data.models.Passage
import tmg.passage.realm.models.RealmPassage
import tmg.passage.realm.models.convert
import tmg.passage.realm.models.saveSync

class RealmPassageConnector(

): PassageConnector {

    override fun allCurrent(): Flow<List<Passage>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun allDone(): Flow<List<Passage>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun allEndingToday(): Flow<List<Passage>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSync(id: String): Passage? {
        val realm: Realm = realm
        val realmPassage = realm.where<RealmPassage>().equalTo("id", id).findFirst()
        val passage = if (realmPassage != null) {
            realm.copyFromRealm(realmPassage)?.convert()
        } else {
            null
        }
        realm.close()
        return passage
    }

    override fun get(id: String): Flow<Passage?> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveSync(passage: Passage) {
        val realm: Realm = realm
        passage.saveSync(realm)
        realm.close()
    }

    override fun save(passage: Passage) {
        TODO()
    }

    override fun deleteAll() {
        realm.executeTransaction {
            it.where<RealmPassage>().findAll().deleteAllFromRealm()
        }
    }

    override fun deleteDone() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(id: String) {
        realm.executeTransaction { transRealm ->
            transRealm.where<RealmPassage>().equalTo("id", id).findFirst()?.deleteFromRealm()
        }
    }

    private val realm: Realm
        get() = Realm.getDefaultInstance()

}