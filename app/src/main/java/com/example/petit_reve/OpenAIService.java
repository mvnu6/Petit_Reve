package com.example.petit_reve;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;

public class OpenAIService {  // Missing class name and incorrect class declaration

    public String getResponse(String prompt) {
        OpenAIClient client = OpenAIOkHttpClient.builder()
                .apiKey(BuildConfig.OPENAI_API_KEY)
                .build();

        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .addUserMessage(prompt)
                .model(ChatModel.GPT_3_5_TURBO)
                .build();

        ChatCompletion completion = client.chat().completions().create(params);
        return filterResponse(completion.choices().get(0).message().content().orElse("Pas de réponse"));  // Added filterResponse call
    }

    /**
     * Cette méthode filtre les réponses générées pour s'assurer qu'elles respectent les critères.
     */
    private String filterResponse(String response) {
        // Liste de mots/clés à éviter (NFT, crypto, contenu inapproprié, etc.)
        String[] inappropriateWords = {
                "NFT", "crypto", "blockchain", "bitcoin", "cryptomonnaie", "adultes", "violence",
                "politique", "guerre", "argent", "sexuel", "porno", "adultère", "racisme", "xénophobie",
                "insultes", "guerre", "terrorisme", "bombe", "arme", "meurtre", "assassinat", "suicide",
                "drogue", "alcool", "tabac", "prostitution", "drogué", "viol", "agression", "harcèlement",
                "nazi", "extrême droite", "extrême gauche", "dictature", "corruption", "escroquerie",
                "escroc", "vol","génocide", "torture", "mendicité",
                "esclavage", "génocide", "armes", "attentat", "tuer", "déstabilisation", "insurrection",
                "combats", "trahison", "terroriste", "harcèlement sexuel", "homophobie", "discrimination",
                "crise économique", "révolution", "rébellion", "attaque", "génocide", "belligérant",
                "hacker", "piratage", "sexisme", "violence domestique", "mutilation", "divorce", "banqueroute",
                "génocide", "raciste", "meurtrier", "traite humaine", "peur", "angoisse", "survie",
                "génocidaire", "prophétie", "armée", "assassiner", "destruction", "explosion", "fuite",
                "secte", "dérive sectaire", "terroriser", "armée", "militarisme", "patriote", "bandit",
                "criminel", "escroquerie", "dictateur", "despote", "monstre", "brutal", "cruel", "boucherie",
                "guérilla", "bourreau", "domination", "violence physique", "sang", "bataille", "agonie",
                "anéantir", "sacrifice", "boucherie", "brutalité", "brutaliser", "répression", "violente",
                "dépression", "obsédé", "stress post-traumatique", "raciste", "terroriste", "expulsion",
                "dictature", "négationnisme", "intolérance", "dérives", "répression", "désastre", "catastrophe",
                // Insultes vulgaires et jurons
                "salope", "connard", "enfoiré", "batard", "merde", "pute", "enculé",
                "enculée", "couille", "saloperie", "sale pute", "sale con", "gros con", "fils de pute",
                "connasse", "clochard", "bordel", "salaud", "putain", "putes", "pédé", "gouine", "enculé",
                "sucer", "cul", "foutre", "nique", "ta mère", "pute de merde", "casseur",
                "merdeux", "c'est de la merde", "connasse", "violence verbale", "racaille", "trou du cul", "la merde", "gros"
        };

        // Vérifier si la réponse contient des mots inappropriés
        for (String word : inappropriateWords) {
            if (response.toLowerCase().contains(word.toLowerCase())) {
                // Remplacer ou ignorer la réponse inappropriée et retourner un message par défaut
                return "Désolé, l'histoire générée ne correspond pas aux critères d'une histoire pour enfants.";
            }
        }
        // 2. Limiter la diversité des sujets
        String[] allowedTopics = {"forêt", "animaux", "magie", "amitié", "aventure", "nature", "étoiles", "rêves",
                "forêt magique", "animaux", "pirates", "merveille", "aventures", "château magique",
                "trésor", "amitié", "monde magique", "animaux qui parlent",
                "animaux fantastiques", "oiseaux", "chatons", "chiens", "pays magique", "jardin enchanté",
                "pays des fées", "papillons", "bonté", "sourires", "ballons", "fleurs", "poules",
                "grande aventure", "compagnons", "exploration", "petits héros", "voyage magique",
                "chemin secret", "forêt enchantée", "lune", "arc-en-ciel", "nuages", "ciel étoilé",
                "saison des fleurs", "animaux du bois", "chanson joyeuse", "feuilles volantes",
                "parc secret", "jouets vivants", "fête", "doudous", "doudou magique", "village caché",
                "lutin", "baleine magique", "nuage magique", "poussin", "moutons", "souris", "licorne",
                "bateau magique", "légendes", "calins", "voiture magique", "le petit train", "arbre enchanté",
                "plage enchantée", "ciel", "moulin à vent", "éléphants", "beaux rêves", "montagne",
                "cheval magique", "chaton aventurier", "coeur", "petit dragon", "baleine dans le ciel",
                "chemin des étoiles", "forêt de bonbons", "animaux qui chantent", "pays des rêves",
                "magicien gentil", "petite aventure", "monstre gentil", "petit prince", "forêt des songes",
                "rire", "sourires", "papillon coloré", "bâton magique", "grandes aventures", "animaux du ciel",
                "poules qui parlent", "aventure de nuit", "pays des rêves", "champ de fleurs", "tapis magique",
                "voyage au pays des fées"};


        boolean containsAllowedTopic = false;
        for (String topic : allowedTopics) {
            if (response.toLowerCase().contains(topic.toLowerCase())) { // Added toLowerCase() for topic
                containsAllowedTopic = true;
                break;
            }
        }
        if (!containsAllowedTopic) {
            return "L'histoire ne doit pas traiter de sujets inappropriés. Essayez avec un sujet plus adapté.";
        }

        // 3. Détection de l'humour inapproprié
        String[] inappropriateHumor = { "humour noir", "blague de mauvais goût", "moquerie", "insulte", "sarcasme",
                "humour sexuel", "humour violent", "humour offensant", "blague raciste", "humour haineux",
                "moquerie sur les autres", "humour cruel", "humour dégradant", "humour sur la souffrance",
                "humour agressif", "humour insultant", "humour dénigrant", "humour sur la violence",
                "humour sur la guerre", "humour sur les accidents", "humour sur les maladies", "blague sur la mort",
                "humour morbide", "humour sur les drogues", "humour sur l'alcool", "humour sur le suicide",
                "blague sur la dépression", "humour sur la pauvreté", "humour sur l'esclavage", "humour sur les victimes",
                "humour sur la criminalité", "humour sur les injustices sociales", "humour sexiste", "humour homophobe",
                "humour raciste", "humour xénophobe", "humour discriminatoire", "humour sur le handicap", "humour sur la religion",
                "humour sur les croyances", "blague sur les enfants", "humour sur les personnes âgées", "humour sur les femmes",
                "humour sur les hommes", "humour sur les animaux", "humour sur les corps", "humour sur les apparences",
                "humour sur les différences physiques", "humour sur les géants", "humour sur les petites tailles", "humour sur la race",
                "humour sur les cultures", "humour sur les traditions", "humour sur les origines", "humour sur les accents",
                "humour sur les vêtements", "humour sur l'apparence physique", "blague sur la sexualité", "humour sur les préférences sexuelles",
                "humour sur les relations", "humour incestueux", "humour sur les enfants handicapés", "humour sur les personnes malades",
                "humour sur les violences domestiques", "humour sur le féminisme", "humour sur l'égalité", "humour sur l'environnement",
                "humour sur les animaux en danger", "humour sur les catastrophes naturelles", "humour sur les tragédies", "humour sur les accidents de voiture",
                "humour sur les morts tragiques", "humour sur les explosions", "humour sur les attentats", "humour sur les tortures",
                "humour sur les traumatismes", "humour sur les guerres", "humour sur les conflits", "humour sur les dictateurs",
                "humour sur les catastrophes humaines", "humour sur les exécutions", "humour sur les tortures publiques",
                "humour sur l'arme nucléaire", "humour sur les victimes d'horreurs", "humour sur la faim", "humour sur la misère",
                "humour sur la pauvreté extrême", "humour sur la dégradation humaine", "humour sur les maltraitances",
                "humour sur les personnes persécutées", "humour sur les abus sexuels", "humour sur la prostitution",
                "humour sur les femmes battues", "humour sur les victimes de viol", "humour sur les crimes de guerre",
                "humour sur les violences policières", "humour sur les agresseurs", "humour sur les victimes de violence",
                "humour sur la guerre des genres", "humour sur les stéréotypes", "humour sur les minorités",
                "humour sur les luttes sociales", "humour sur les révoltes", "humour sur la répression", "humour sur l'injustice",
                "humour sur les inégalités sociales", "humour sur l'exclusion sociale", "humour sur les personnes sans-abri",
                "humour sur les travailleurs pauvres", "humour sur les sans-papiers", "humour sur les immigrants", "humour sur les réfugiés",
                "humour sur l'exploitation", "humour sur les drames familiaux", "humour sur les traumatismes familiaux",
                "humour sur la violence dans les familles", "humour sur les secrets de famille", "humour sur les relations toxiques",
                "humour sur les mariages forcés", "humour sur les malheurs", "humour sur les souffrances", "humour sur les pathologies mentales"
        };

        for (String word : inappropriateHumor) {
            if (response.toLowerCase().contains(word.toLowerCase())) {
                return "L'humour utilisé n'est pas adapté pour un jeune public.";
            }
        }

        // Commented out code for word count check - left for reference
        /*
        String[] commonWords = {"chat", "forêt", "magie"};
        for (String word : commonWords) {
            if (countOccurrences(response, word) > 3) {
                return "L'histoire semble un peu répétitive, essayons quelque chose de plus varié.";
            }
        }
        */

        // Vérification des émotions exprimées dans l'histoire
        String[] negativeEmotions = {"triste", "colère", "peur", "mort", "agonie"};
        for (String word : negativeEmotions) {
            if (response.toLowerCase().contains(word.toLowerCase())) {
                return "L'histoire ne doit pas contenir d'émotions négatives ou inquiétantes.";
            }
        }


        // Vérification de ton : Assurez-vous que la réponse est appropriée pour les jeunes enfants
        if (!isResponseChildFriendly(response)) {
            // Si le ton n'est pas adapté, retourner un message générique ou une réponse par défaut
            return "Désolé, l'histoire générée n'est pas adaptée aux enfants.";
        }

        // Si la réponse passe tous les filtres, la retourner
        return response;
    }

    /**
     * Méthode pour vérifier que la réponse est adaptée aux enfants (ton rassurant et simple)
     */
    private boolean isResponseChildFriendly(String response) {
        // Exemples de vérifications de ton (si la réponse contient des mots trop complexes ou durs)
        String[] negativeWords = {"violence", "danger", "peur", "mort", "mortality", "combat"};
        for (String word : negativeWords) {
            if (response.toLowerCase().contains(word.toLowerCase())) {
                return false;  // Si la réponse contient un mot inapproprié, elle n'est pas adaptée
            }
        }
        return true; // Si aucune vérification n'échoue, la réponse est considérée comme appropriée
    }

    // Added method that was referenced but missing
    private int countOccurrences(String text, String word) {
        int count = 0;
        String lowerText = text.toLowerCase();
        String lowerWord = word.toLowerCase();
        int index = lowerText.indexOf(lowerWord);

        while (index != -1) {
            count++;
            index = lowerText.indexOf(lowerWord, index + lowerWord.length());
        }

        return count;
    }
}