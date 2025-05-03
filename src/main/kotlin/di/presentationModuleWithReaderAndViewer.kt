package di

import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.input.ReaderImpl
import org.baghdad.presentation.output.ViewerImpl
import org.baghdad.presentation.output.Viewer
import org.koin.dsl.module


val presentationModuleWithReaderAndViewer = module {
    single<Reader> { ReaderImpl() }
    single<Viewer> { ViewerImpl() }
}
