package com.opurex.ortus.client.utils.file;

import android.content.Context;
import com.opurex.ortus.client.OrtusPOS;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by nsvir on 19/10/15.
 * n.svirchevsky@gmail.com
 */
public class InternalFile extends File {

    public InternalFile(String fileName) {
        super(OrtusPOS.getAppContext().getFilesDir(), fileName);
    }

    public InternalFile(String path, String fileName) {
        super(OrtusPOS.getAppContext().getDir(path, Context.MODE_PRIVATE), fileName);
    }

    public InternalFile(String dir, int directory) {
        super(dir, directory);
    }

    protected FileInputStream openRead() throws FileNotFoundException {
        return new FileInputStream(this);
    }

    protected FileOutputStream openWrite() throws FileNotFoundException {
        return new FileOutputStream(this);
    }
}
