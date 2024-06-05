package com.example.common_lib.http.converter

import com.example.common_lib.http.utils.JsonUtil
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.Buffer
import retrofit2.Converter
import java.io.OutputStreamWriter
import java.io.Writer
import java.lang.reflect.Type
import java.nio.charset.Charset

class GsonRequestBodyConverter<T>(
    type: Type
) : Converter<T, RequestBody> {

    private val gson: Gson = JsonUtil.gson
    private val adapter: TypeAdapter<T> = gson.getAdapter(TypeToken.get(type)) as TypeAdapter<T>

    override fun convert(value: T): RequestBody? {
        val buffer = Buffer()
        val writer: Writer =
            OutputStreamWriter(buffer.outputStream(), Charset.forName("UTF-8"))
        val jsonWriter = gson.newJsonWriter(writer)
        adapter.write(jsonWriter, value)
        jsonWriter.close()
        return RequestBody.create(
            MediaType.get("application/json; charset=UTF-8"),
            buffer.readByteString()
        )
    }
}