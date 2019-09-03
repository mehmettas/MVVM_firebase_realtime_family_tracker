package com.mehmettas.familytrack.data.repository

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.mehmettas.familytrack.data.remote.RemoteDataManager
import com.mehmettas.familytrack.data.remote.model.family.Family

class DataManager(
    private val remoteDataManager: RemoteDataManager): IDataManager {

    override suspend fun writeOnFamily(
        model: Any,
        documentReference: DocumentReference,
        success: Any,
        failure: Any,
        navigator: Any
    ) = remoteDataManager.writeOnFamily(model,documentReference,success,failure,navigator)

    override suspend fun createFamily(
        model: Any,
        documentReference: DocumentReference,
        success:Any,
        failure:Any,
        navigator: Any
    ) = remoteDataManager.createFamily(model,documentReference,success,failure,navigator)

    override suspend fun isDocumentExist(
        documentReference: DocumentReference,
        isExist:Any,
        notExist: Any,
        navigator: Any
    ) = remoteDataManager.isDocumentExist(documentReference,isExist,notExist,navigator)

    override suspend fun retriveFamily(
        documentReference: DocumentReference,
        isExist: Any,
        notExist: Any,
        navigator: Any
    ) = remoteDataManager.retriveFamily(documentReference,isExist,notExist,navigator)

    override suspend fun retriveFamilyMembers(
        collectionReference: CollectionReference,
        familyId:String,
        membersRetrieved: Any,
        membersNotRetrieved: Any,
        navigator: Any
    ) = remoteDataManager.retriveFamilyMembers(collectionReference,familyId,membersRetrieved,membersNotRetrieved,navigator)

    override suspend fun setCurrentUserLocation(
        modelData: Any,
        documentReference: DocumentReference,
        userLocationSetSuccess: Any,
        userLocationSetFailure: Any,
        navigator: Any
    ) = remoteDataManager.setCurrentUserLocation(modelData,documentReference,userLocationSetSuccess,userLocationSetFailure,navigator)

    override suspend fun listenForFamilyMemberLocations(
        documentReferences: ArrayList<DocumentReference>,
        listenSuccess: Any,
        listenFailure: Any,
        navigator: Any
    ) = remoteDataManager.listenForFamilyMemberLocations(documentReferences,listenSuccess,listenFailure,navigator)

    override suspend fun retriveSingleMember(
        documentReference: DocumentReference,
        isExist: Any,
        notExist: Any,
        navigator: Any
    ) = remoteDataManager.retriveSingleMember(documentReference,isExist,notExist,navigator)
}